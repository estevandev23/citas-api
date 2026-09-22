package co.edu.fcv.citas.adapter.persistence;

import co.edu.fcv.citas.application.port.RefreshSessions;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
class RefreshPersistenceAdapter implements RefreshSessions {
    private final JpaRefreshSessions repo;
    RefreshPersistenceAdapter(JpaRefreshSessions repo) { this.repo = repo; }
    public void save(Long userId, String hash, Instant expiresAt) {
        var session = new RefreshSessionEntity(); session.userId = userId;
        session.tokenIdHash = hash; session.expiresAt = expiresAt;
        repo.save(session);
    }
    public boolean consume(Long userId, String hash, Instant now) { return repo.consume(userId, hash, now) == 1; }
}
