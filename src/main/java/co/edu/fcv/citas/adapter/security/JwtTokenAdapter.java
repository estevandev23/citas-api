package co.edu.fcv.citas.adapter.security;

import co.edu.fcv.citas.application.IdentityException;
import co.edu.fcv.citas.application.port.TokenService;
import co.edu.fcv.citas.domain.UserAccount;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Qualifier;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Component;

@Component
class JwtTokenAdapter implements TokenService {
    private final NimbusJwtEncoder accessEncoder;
    private final NimbusJwtEncoder refreshEncoder;
    private final NimbusJwtDecoder refreshDecoder;
    private final Clock clock;
    private final Duration accessLifetime;
    private final Duration refreshLifetime;

    JwtTokenAdapter(@Qualifier("accessKey") SecretKey accessKey, @Qualifier("refreshKey") SecretKey refreshKey, Clock clock,
                    @Value("${app.jwt.access-minutes}") long accessMinutes,
                    @Value("${app.jwt.refresh-days}") long refreshDays) {
        if (accessMinutes < 1 || refreshDays < 1) throw new IllegalArgumentException("JWT lifetimes must be positive");
        this.accessEncoder = new NimbusJwtEncoder(new ImmutableSecret<>(accessKey));
        this.refreshEncoder = new NimbusJwtEncoder(new ImmutableSecret<>(refreshKey));
        this.refreshDecoder = NimbusJwtDecoder.withSecretKey(refreshKey).macAlgorithm(MacAlgorithm.HS256).build();
        this.clock = clock;
        this.accessLifetime = Duration.ofMinutes(accessMinutes);
        this.refreshLifetime = Duration.ofDays(refreshDays);
    }

    public IssuedTokens issue(UserAccount user) {
        Instant now = clock.instant();
        String accessId = UUID.randomUUID().toString();
        String refreshId = UUID.randomUUID().toString();
        Instant refreshExpiry = now.plus(refreshLifetime);
        var accessClaims = JwtClaimsSet.builder().subject(user.id().toString()).issuedAt(now)
                .expiresAt(now.plus(accessLifetime)).id(accessId).claim("type", "access")
                .claim("roles", user.roles()).build();
        var refreshClaims = JwtClaimsSet.builder().subject(user.id().toString()).issuedAt(now)
                .expiresAt(refreshExpiry).id(refreshId).claim("type", "refresh").build();
        var header = JwsHeader.with(MacAlgorithm.HS256).build();
        return new IssuedTokens(accessEncoder.encode(JwtEncoderParameters.from(header, accessClaims)).getTokenValue(),
                refreshEncoder.encode(JwtEncoderParameters.from(header, refreshClaims)).getTokenValue(),
                refreshExpiry, refreshId);
    }

    public RefreshIdentity validateRefresh(String token) {
        try {
            var jwt = refreshDecoder.decode(token);
            if (!"refresh".equals(jwt.getClaimAsString("type")) || jwt.getId() == null)
                throw new IllegalArgumentException("Wrong token type");
            return new RefreshIdentity(Long.parseLong(jwt.getSubject()), jwt.getId());
        } catch (Exception ex) {
            throw new IdentityException("INVALID_REFRESH", "Sesión no válida");
        }
    }

    public String hashId(String tokenId) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(tokenId.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException ex) { throw new IllegalStateException(ex); }
    }
}
