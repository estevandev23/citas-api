package co.edu.fcv.citas.adapter.security;

import co.edu.fcv.citas.application.port.PasswordService;
import co.edu.fcv.citas.application.port.PasswordResetTokens;
import co.edu.fcv.citas.application.port.RecoveryTokenGenerator;
import co.edu.fcv.citas.application.IdentityService;
import co.edu.fcv.citas.application.FixedCatalogService;
import co.edu.fcv.citas.application.ProfileService;
import co.edu.fcv.citas.application.port.FixedCatalogs;
import co.edu.fcv.citas.application.port.UserProfiles;
import co.edu.fcv.citas.application.port.UserAccounts;
import co.edu.fcv.citas.application.port.RefreshSessions;
import co.edu.fcv.citas.application.port.TokenService;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {
    @Bean Clock clock() { return Clock.systemUTC(); }

    @Bean IdentityService identityService(UserAccounts users, PasswordService passwords,
                                          TokenService tokens, RefreshSessions sessions,
                                          PasswordResetTokens resetTokens, RecoveryTokenGenerator recoveryTokenGenerator,
                                          Clock clock) {
        return new IdentityService(users, passwords, tokens, sessions, resetTokens, recoveryTokenGenerator, clock);
    }

    @Bean FixedCatalogService fixedCatalogService(FixedCatalogs catalogs) {
        return new FixedCatalogService(catalogs);
    }
    @Bean ProfileService profileService(UserProfiles profiles) { return new ProfileService(profiles); }

    @Bean PasswordService passwordService() {
        var encoder = new BCryptPasswordEncoder();
        return new PasswordService() {
            public String hash(String raw) { return encoder.encode(raw); }
            public boolean matches(String raw, String hash) { return encoder.matches(raw, hash); }
        };
    }

    @Bean SecretKey accessKey(@Value("${app.jwt.access-secret}") String secret) { return key(secret); }
    @Bean SecretKey refreshKey(@Value("${app.jwt.refresh-secret}") String secret) { return key(secret); }

    private static SecretKey key(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) throw new IllegalArgumentException("JWT secret must contain at least 32 bytes");
        return new SecretKeySpec(bytes, "HmacSHA256");
    }

    @Bean JwtDecoder jwtDecoder(@Qualifier("accessKey") SecretKey accessKey) {
        var decoder = NimbusJwtDecoder.withSecretKey(accessKey).macAlgorithm(MacAlgorithm.HS256).build();
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(JwtValidators.createDefault(), jwt ->
                "access".equals(jwt.getClaimAsString("type")) ? OAuth2TokenValidatorResult.success() :
                        OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Not an access token", null))));
        return decoder;
    }

    @Bean SecurityFilterChain filterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> jwt.getClaimAsStringList("roles").stream()
                .<GrantedAuthority>map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList());
        return http.csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/register", "/api/v1/auth/login", "/api/v1/auth/refresh",
                                "/api/v1/auth/logout", "/api/v1/auth/password-recovery", "/api/v1/auth/password-reset", "/api/v1/auth/bootstrap-admin").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.decoder(jwtDecoder).jwtAuthenticationConverter(converter)))
                .build();
    }

    @Bean CorsConfigurationSource corsConfigurationSource(@Value("${app.frontend-origin}") String origin) {
        var config = new CorsConfiguration();
        config.setAllowedOrigins(java.util.List.of(origin));
        config.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(java.util.List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
