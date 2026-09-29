package co.edu.fcv.citas;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
class SchedulingFlowTest {
    private static final String ACCESS_SECRET = secret();
    private static final String REFRESH_SECRET = secret();
    private static String secret() { byte[] bytes = new byte[48]; new SecureRandom().nextBytes(bytes); return Base64.getEncoder().encodeToString(bytes); }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> "jdbc:h2:mem:citas_scheduling;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1");
        registry.add("spring.datasource.username", () -> "sa");
        registry.add("spring.datasource.password", () -> "");
        registry.add("spring.datasource.driver-class-name", () -> "org.h2.Driver");
        registry.add("app.jwt.access-secret", () -> ACCESS_SECRET);
        registry.add("app.jwt.refresh-secret", () -> REFRESH_SECRET);
    }

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired JdbcTemplate jdbc;

    private Map<String, String> user(String role, String suffix) {
        Map<String, String> body = new HashMap<>();
        body.put("firstName", role); body.put("lastName", "Sintetico"); body.put("documentType", "CC");
        body.put("documentNumber", role + "-" + suffix); body.put("email", role.toLowerCase() + "-" + suffix + "@example.test");
        body.put("phone", "3000000000"); body.put("password", "CitasDemo123!"); return body;
    }
    private JsonNode parse(MvcResult result) throws Exception { return json.readTree(result.getResponse().getContentAsString()); }
    private MvcResult register(Map<String, String> body) throws Exception { return mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(body))).andReturn(); }
    private String login(Map<String, String> body) throws Exception {
        MvcResult result = mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(Map.of("email", body.get("email"), "password", body.get("password"))))).andReturn();
        return parse(result).get("accessToken").asText();
    }
    private MvcResult postJson(String path, String token, Object body) throws Exception { return mvc.perform(post(path).header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(body))).andReturn(); }

    private Fixture fixture(String suffix) throws Exception {
        Map<String, String> admin = user("Admin", suffix), patient = user("Patient", suffix), second = user("Second", suffix), professional = user("Professional", suffix);
        long adminId = parse(register(admin)).get("id").asLong(); register(patient); register(second); long professionalUserId = parse(register(professional)).get("id").asLong();
        jdbc.update("INSERT INTO user_role(user_id,role_code) VALUES(?, 'ADMIN')", adminId);
        String adminToken = login(admin), patientToken = login(patient), secondToken = login(second);
        JsonNode created = parse(postJson("/api/v1/admin/professionals", adminToken, Map.of("userId", professionalUserId, "professionalCode", "PRO-" + suffix, "licenseNumber", "LIC-" + suffix, "specialtyCodes", java.util.List.of("MEDICINA_GENERAL", "NEUROLOGIA"), "facilityCodes", java.util.List.of("HIC"))));
        long professionalId = created.get("id").asLong();
        String professionalToken = login(professional);
        return new Fixture(adminToken, patientToken, secondToken, professionalToken, professionalId);
    }
    private record Fixture(String admin, String patient, String secondPatient, String professional, long professionalId) {}

    @Test
    void patientCannotManageProfessionalAvailability() throws Exception {
        Fixture f = fixture(UUID.randomUUID().toString().substring(0, 8));
        Map<String, Object> block = Map.of("facilityCode", "HIC", "date", LocalDate.now().plusDays(3), "startTime", "08:00:00", "endTime", "09:00:00");
        mvc.perform(post("/api/v1/professional/availability").header("Authorization", "Bearer " + f.patient()).contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(block))).andExpect(status().isForbidden());
        mvc.perform(post("/api/v1/professional/availability").contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(block))).andExpect(status().isUnauthorized());
    }

    @Test
    void generalBookingAutoApprovesAndDoubleBookingIsRejected() throws Exception {
        Fixture f = fixture(UUID.randomUUID().toString().substring(0, 8)); LocalDate day = LocalDate.now().plusDays(4); LocalDateTime start = day.atTime(8, 0);
        postJson("/api/v1/professional/availability", f.professional(), Map.of("facilityCode", "HIC", "date", day, "startTime", "08:00:00", "endTime", "10:00:00"));
        MvcResult first = postJson("/api/v1/appointments", f.patient(), Map.of("facilityCode", "HIC", "specialtyCode", "MEDICINA_GENERAL", "professionalId", f.professionalId(), "startAt", start));
        assertThat(first.getResponse().getStatus()).isEqualTo(200); assertThat(parse(first).get("status").asText()).isEqualTo("APPROVED");
        MvcResult second = postJson("/api/v1/appointments", f.secondPatient(), Map.of("facilityCode", "HIC", "specialtyCode", "MEDICINA_GENERAL", "professionalId", f.professionalId(), "startAt", start));
        assertThat(second.getResponse().getStatus()).isEqualTo(400); assertThat(parse(second).get("code").asText()).isEqualTo("SLOT_UNAVAILABLE");
    }

    @Test
    void specializedRejectionRequiresReasonAndReleasesSlot() throws Exception {
        Fixture f = fixture(UUID.randomUUID().toString().substring(0, 8)); LocalDate day = LocalDate.now().plusDays(5); LocalDateTime start = day.atTime(10, 0);
        postJson("/api/v1/professional/availability", f.professional(), Map.of("facilityCode", "HIC", "date", day, "startTime", "10:00:00", "endTime", "11:00:00"));
        MvcResult requested = postJson("/api/v1/appointments", f.patient(), Map.of("facilityCode", "HIC", "specialtyCode", "NEUROLOGIA", "professionalId", f.professionalId(), "startAt", start));
        long appointmentId = parse(requested).get("id").asLong(); assertThat(parse(requested).get("status").asText()).isEqualTo("REQUESTED");
        MvcResult missingReason = mvc.perform(post("/api/v1/admin/appointments/" + appointmentId + "/decision").header("Authorization", "Bearer " + f.admin()).queryParam("approve", "false")).andReturn();
        assertThat(missingReason.getResponse().getStatus()).isEqualTo(400); assertThat(parse(missingReason).get("code").asText()).isEqualTo("REASON_REQUIRED");
        mvc.perform(post("/api/v1/admin/appointments/" + appointmentId + "/decision").header("Authorization", "Bearer " + f.admin()).queryParam("approve", "false").queryParam("reason", "No disponibilidad confirmada")).andExpect(status().isOk());
        mvc.perform(get("/api/v1/availability").header("Authorization", "Bearer " + f.patient()).param("facilityCode", "HIC").param("specialtyCode", "NEUROLOGIA").param("date", day.toString())).andExpect(status().isOk());
    }
}
