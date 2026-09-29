package co.edu.fcv.citas.application.port;

import java.time.Instant;
import java.util.Optional;

public interface PasswordResetTokens {
    void save(Long userId, String tokenHash, Instant expiresAt);
    Optional<Long> consume(String tokenHash, Instant now);
}
