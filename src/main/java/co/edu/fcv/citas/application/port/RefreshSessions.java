package co.edu.fcv.citas.application.port;

import java.time.Instant;

public interface RefreshSessions {
    void save(Long userId, String tokenIdHash, Instant expiresAt);
    boolean consume(Long userId, String tokenIdHash, Instant now);
}
