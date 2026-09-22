package co.edu.fcv.citas.adapter.persistence;

import java.time.Instant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface JpaRefreshSessions extends JpaRepository<RefreshSessionEntity, Long> {
    @Modifying
    @Query("update RefreshSessionEntity s set s.consumedAt = :now where s.userId = :userId and s.tokenIdHash = :hash and s.consumedAt is null and s.expiresAt > :now")
    int consume(@Param("userId") Long userId, @Param("hash") String hash, @Param("now") Instant now);
}
