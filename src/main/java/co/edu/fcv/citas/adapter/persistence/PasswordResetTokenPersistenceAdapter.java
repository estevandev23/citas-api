package co.edu.fcv.citas.adapter.persistence;

import co.edu.fcv.citas.application.port.PasswordResetTokens;
import java.time.Instant;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
class PasswordResetTokenPersistenceAdapter implements PasswordResetTokens {
    private final JpaPasswordResetTokens repo;

    PasswordResetTokenPersistenceAdapter(JpaPasswordResetTokens repo) { this.repo = repo; }

    public void save(Long userId, String tokenHash, Instant expiresAt) {
        var token = new PasswordResetTokenEntity();
        token.userId = userId; token.tokenHash = tokenHash; token.expiresAt = expiresAt;
        repo.save(token);
    }

    public Optional<Long> consume(String tokenHash, Instant now) {
        return repo.findAvailable(tokenHash, now)
                .filter(token -> repo.consume(token.id, now) == 1)
                .map(token -> token.userId);
    }
}
