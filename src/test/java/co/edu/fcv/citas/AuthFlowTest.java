package co.edu.fcv.citas;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Map;
import java.util.UUID;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
class AuthFlowTest {
    private static final String ACCESS_SECRET = randomSecret();
    private static final String REFRESH_SECRET = randomSecret();
    private static String randomSecret() {
        byte[] bytes = new byte[48]; new SecureRandom().nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> "jdbc:h2:mem:citas_auth;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1");
        registry.add("spring.datasource.username", () -> "sa");
        registry.add("spring.datasource.password", () -> "");
        registry.add("spring.datasource.driver-class-name", () -> "org.h2.Driver");
        registry.add("app.jwt.access-secret", () -> ACCESS_SECRET);
        registry.add("app.jwt.refresh-secret", () -> REFRESH_SECRET);
        registry.add("app.password-recovery.expose-token", () -> true);
    }

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired JdbcTemplate jdbc;

    private Map<String, String> user(String suffix) {
        return Map.of("firstName", "Paciente", "lastName", "Ficticio", "documentType", "CC",
                "documentNumber", suffix, "email", "paciente-" + suffix + "@example.test",
                "phone", "3000000000", "password", "claveDePrueba123!");
    }
    private MvcResult register(Map<String, String> body) throws Exception {
        return mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(body))).andReturn();
    }
    private MvcResult postJson(String path, Object body) throws Exception {
        return mvc.perform(post(path).contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(body))).andReturn();
    }
    private JsonNode parse(MvcResult result) throws Exception { return json.readTree(result.getResponse().getContentAsString()); }

    @Test
    void registrationIsUniqueAndPasswordIsHashed() throws Exception {
        var body = user(UUID.randomUUID().toString());
        var created = register(body);
        assertThat(created.getResponse().getStatus()).isEqualTo(201);
        assertThat(parse(created).get("roles").get(0).asText()).isEqualTo("USER");
        assertThat(created.getResponse().getContentAsString()).doesNotContain("password", "claveDePrueba123!");
        String hash = jdbc.queryForObject("select password_hash from app_user where email = ?", String.class, body.get("email"));
        assertThat(hash).isNotEqualTo(body.get("password"));
        assertThat(new BCryptPasswordEncoder().matches(body.get("password"), hash)).isTrue();

        var duplicateEmail = new java.util.HashMap<>(body);
        duplicateEmail.put("documentNumber", UUID.randomUUID().toString());
        assertThat(parse(register(duplicateEmail)).get("code").asText()).isEqualTo("EMAIL_EXISTS");
        var duplicateDocument = new java.util.HashMap<>(body);
        duplicateDocument.put("email", "other-" + UUID.randomUUID() + "@example.test");
        assertThat(parse(register(duplicateDocument)).get("code").asText()).isEqualTo("DOCUMENT_EXISTS");
        assertThat(jdbc.queryForObject("select count(*) from app_user where document_number = ?", Integer.class,
                body.get("documentNumber"))).isEqualTo(1);
    }

    @Test
    void invalidRegistrationReportsFieldsAndDoesNotPersist() throws Exception {
        var body = new java.util.HashMap<>(user(UUID.randomUUID().toString()));
        body.put("email", "invalid"); body.put("firstName", " ");
        var response = register(body);
        assertThat(response.getResponse().getStatus()).isEqualTo(400);
        assertThat(parse(response).get("fields").toString()).contains("email", "firstName");
        assertThat(jdbc.queryForObject("select count(*) from app_user where document_number = ?", Integer.class,
                body.get("documentNumber"))).isZero();
    }

    @Test
    void fixedCatalogsAreSeededReadableAndHaveNoWriteEndpoint() throws Exception {
        var body = user(UUID.randomUUID().toString());
        register(body);
        String access = parse(postJson("/api/v1/auth/login", Map.of("email", body.get("email"),
                "password", body.get("password")))).get("accessToken").asText();

        mvc.perform(get("/api/v1/catalogs/fixed")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/v1/catalogs/fixed").header("Authorization", "Bearer " + access))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roles[0].code").value("USER"))
                .andExpect(jsonPath("$.appointmentStatuses[0].code").value("REQUESTED"))
                .andExpect(jsonPath("$.reschedulingStatuses[0].code").value("PENDING"))
                .andExpect(jsonPath("$.regimes[0].code").value("CONTRIBUTORY"))
                .andExpect(jsonPath("$.facilities[0].code").value("HIC"));

        mvc.perform(post("/api/v1/catalogs/fixed").header("Authorization", "Bearer " + access))
                .andExpect(status().isMethodNotAllowed());
        assertThat(jdbc.queryForObject("select count(*) from facility", Integer.class)).isEqualTo(2);
    }

    @Test
    void userCanOnlyReadAndUpdateTheirOwnNormalizedProfile() throws Exception {
        var body = user(UUID.randomUUID().toString());
        register(body);
        String access = parse(postJson("/api/v1/auth/login", Map.of("email", body.get("email"),
                "password", body.get("password")))).get("accessToken").asText();

        mvc.perform(get("/api/v1/catalogs/affiliation-options").header("Authorization", "Bearer " + access))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.insurers[0].code").value("EPS_LAB"))
                .andExpect(jsonPath("$.plans[0].insurerCode").value("EPS_LAB"));
        mvc.perform(get("/api/v1/profile/me").header("Authorization", "Bearer " + access))
                .andExpect(status().isOk()).andExpect(jsonPath("$.email").value(body.get("email")))
                .andExpect(jsonPath("$.insurerCode").doesNotExist());
        mvc.perform(put("/api/v1/profile/me").header("Authorization", "Bearer " + access)
                .contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(Map.of(
                        "firstName", "Paciente actualizado", "lastName", "Ficticio", "phone", "3001112233",
                        "insurerCode", "EPS_LAB", "planCode", "PLUS", "regimeCode", "CONTRIBUTORY"))))
                .andExpect(status().isOk()).andExpect(jsonPath("$.firstName").value("Paciente actualizado"))
                .andExpect(jsonPath("$.planCode").value("PLUS"));
        assertThat(jdbc.queryForObject("select count(*) from user_affiliation", Integer.class)).isEqualTo(1);
        mvc.perform(put("/api/v1/profile/me").header("Authorization", "Bearer " + access)
                .contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(Map.of(
                        "firstName", "Paciente", "lastName", "Ficticio", "phone", "3001112233",
                        "insurerCode", "EPS_LAB", "planCode", "UNKNOWN", "regimeCode", "CONTRIBUTORY"))))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value("INVALID_AFFILIATION"));
    }

    @Test
    void loginAndRefreshRotateTokensAndRejectInvalidCases() throws Exception {
        var body = user(UUID.randomUUID().toString());
        register(body);
        var login = postJson("/api/v1/auth/login", Map.of("email", body.get("email"), "password", body.get("password")));
        assertThat(login.getResponse().getStatus()).isEqualTo(200);
        var loginJson = parse(login);
        String access = loginJson.get("accessToken").asText();
        String refresh = loginJson.get("refreshToken").asText();
        assertThat(access).isNotEqualTo(refresh);
        var refreshCookie = login.getResponse().getCookie("citas_refresh");
        assertThat(refreshCookie).isNotNull();
        assertThat(refreshCookie.isHttpOnly()).isTrue();

        mvc.perform(get("/api/v1/session/me").header("Authorization", "Bearer " + access))
                .andExpect(status().isOk()).andExpect(jsonPath("$.roles[0]").value("USER"));
        mvc.perform(get("/api/v1/session/me").header("Authorization", "Bearer " + refresh))
                .andExpect(status().isUnauthorized());
        assertThat(postJson("/api/v1/auth/login", Map.of("email", body.get("email"), "password", "wrong"))
                .getResponse().getStatus()).isEqualTo(401);
        assertThat(postJson("/api/v1/auth/login", Map.of("email", "missing@example.test", "password", "wrong"))
                .getResponse().getStatus()).isEqualTo(401);

        var renewed = postJson("/api/v1/auth/refresh", Map.of("refreshToken", refresh));
        assertThat(renewed.getResponse().getStatus()).isEqualTo(200);
        assertThat(parse(renewed).get("refreshToken").asText()).isNotEqualTo(refresh);
        mvc.perform(get("/api/v1/session/me").header("Authorization", "Bearer " + parse(renewed).get("accessToken").asText()))
                .andExpect(status().isOk());
        assertThat(postJson("/api/v1/auth/refresh", Map.of("refreshToken", refresh)).getResponse().getStatus()).isEqualTo(401);
        assertThat(postJson("/api/v1/auth/refresh", Map.of("refreshToken", access)).getResponse().getStatus()).isEqualTo(401);
        assertThat(postJson("/api/v1/auth/refresh", Map.of("refreshToken", "not-a-token")).getResponse().getStatus()).isEqualTo(401);

        var cookieLogin = postJson("/api/v1/auth/login", Map.of("email", body.get("email"), "password", body.get("password")));
        var cookieRefresh = cookieLogin.getResponse().getCookie("citas_refresh");
        assertThat(mvc.perform(post("/api/v1/auth/refresh").cookie(cookieRefresh))
                .andExpect(status().isOk()).andReturn().getResponse().getCookie("citas_refresh")).isNotNull();
    }

    @Test
    void expiredRefreshIsRejectedEvenWhenItsSessionExists() throws Exception {
        var body = user(UUID.randomUUID().toString());
        var registered = parse(register(body));
        long userId = registered.get("id").asLong();
        String tokenId = UUID.randomUUID().toString();
        var secretKey = new SecretKeySpec(REFRESH_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        var encoder = new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));
        var claims = JwtClaimsSet.builder().subject(Long.toString(userId)).id(tokenId)
                .issuedAt(Instant.now().minusSeconds(3600)).expiresAt(Instant.now().minusSeconds(600))
                .claim("type", "refresh").build();
        String token = encoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();
        String hash = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                .digest(tokenId.getBytes(StandardCharsets.UTF_8)));
        jdbc.update("insert into refresh_session (user_id, token_id_hash, expires_at) values (?, ?, ?)",
                userId, hash, java.sql.Timestamp.from(Instant.now().plusSeconds(3600)));
        assertThat(postJson("/api/v1/auth/refresh", Map.of("refreshToken", token)).getResponse().getStatus()).isEqualTo(401);
    }

    @Test
    void logoutRevokesOnlyTheSubmittedRefreshAndIsIdempotent() throws Exception {
        var body = user(UUID.randomUUID().toString());
        register(body);
        String refresh = parse(postJson("/api/v1/auth/login", Map.of("email", body.get("email"),
                "password", body.get("password")))).get("refreshToken").asText();

        mvc.perform(post("/api/v1/auth/logout").contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(Map.of("refreshToken", refresh))))
                .andExpect(status().isNoContent());
        assertThat(postJson("/api/v1/auth/refresh", Map.of("refreshToken", refresh)).getResponse().getStatus()).isEqualTo(401);
        mvc.perform(post("/api/v1/auth/logout").contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(Map.of("refreshToken", refresh))))
                .andExpect(status().isNoContent());
        mvc.perform(post("/api/v1/auth/logout").contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(Map.of("refreshToken", "not-a-token"))))
                .andExpect(status().isNoContent());
    }

    @Test
    void passwordResetIsSingleUseAndRevokesExistingRefreshSessions() throws Exception {
        var body = user(UUID.randomUUID().toString());
        register(body);
        String refresh = parse(postJson("/api/v1/auth/login", Map.of("email", body.get("email"),
                "password", body.get("password")))).get("refreshToken").asText();

        var recovery = postJson("/api/v1/auth/password-recovery", Map.of("email", body.get("email")));
        assertThat(recovery.getResponse().getStatus()).isEqualTo(202);
        String resetToken = parse(recovery).get("resetToken").asText();
        String storedHash = jdbc.queryForObject("select token_hash from password_reset_token order by id desc limit 1", String.class);
        assertThat(storedHash).hasSize(64).isNotEqualTo(resetToken);

        assertThat(postJson("/api/v1/auth/password-reset", Map.of("token", resetToken,
                "newPassword", "claveNueva123!")).getResponse().getStatus()).isEqualTo(204);
        assertThat(postJson("/api/v1/auth/login", Map.of("email", body.get("email"),
                "password", body.get("password"))).getResponse().getStatus()).isEqualTo(401);
        assertThat(postJson("/api/v1/auth/login", Map.of("email", body.get("email"),
                "password", "claveNueva123!")).getResponse().getStatus()).isEqualTo(200);
        assertThat(postJson("/api/v1/auth/refresh", Map.of("refreshToken", refresh)).getResponse().getStatus()).isEqualTo(401);
        var repeated = postJson("/api/v1/auth/password-reset", Map.of("token", resetToken,
                "newPassword", "otraClave123!"));
        assertThat(repeated.getResponse().getStatus()).isEqualTo(400);
        assertThat(parse(repeated).get("code").asText()).isEqualTo("INVALID_RESET_TOKEN");
    }

    @Test
    void expiredOrUnknownPasswordRecoveryTokensAreRejectedWithoutCreatingAUser() throws Exception {
        var body = user(UUID.randomUUID().toString());
        register(body);
        String resetToken = parse(postJson("/api/v1/auth/password-recovery", Map.of("email", body.get("email"))))
                .get("resetToken").asText();
        jdbc.update("update password_reset_token set expires_at = ?", java.sql.Timestamp.from(Instant.now().minusSeconds(1)));
        assertThat(postJson("/api/v1/auth/password-reset", Map.of("token", resetToken,
                "newPassword", "claveNueva123!")).getResponse().getStatus()).isEqualTo(400);
        var unknown = postJson("/api/v1/auth/password-recovery", Map.of("email", "missing@example.test"));
        assertThat(unknown.getResponse().getStatus()).isEqualTo(202);
        assertThat(unknown.getResponse().getContentAsString()).isBlank();
    }
}
