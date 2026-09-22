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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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
    void loginAndRefreshRotateTokensAndRejectInvalidCases() throws Exception {
        var body = user(UUID.randomUUID().toString());
        register(body);
        var login = postJson("/api/v1/auth/login", Map.of("email", body.get("email"), "password", body.get("password")));
        assertThat(login.getResponse().getStatus()).isEqualTo(200);
        var loginJson = parse(login);
        String access = loginJson.get("accessToken").asText();
        String refresh = loginJson.get("refreshToken").asText();
        assertThat(access).isNotEqualTo(refresh);

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
}
