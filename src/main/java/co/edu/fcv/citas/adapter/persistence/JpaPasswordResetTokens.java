package co.edu.fcv.citas.adapter.persistence;

import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface JpaPasswordResetTokens extends JpaRepository<PasswordResetTokenEntity, Long> {
    @Query("select t from PasswordResetTokenEntity t where t.tokenHash = :hash and t.consumedAt is null and t.expiresAt > :now")
    Optional<PasswordResetTokenEntity> findAvailable(@Param("hash") String hash, @Param("now") Instant now);

    @Modifying
    @Query("update PasswordResetTokenEntity t set t.consumedAt = :now where t.id = :id and t.consumedAt is null and t.expiresAt > :now")
    int consume(@Param("id") Long id, @Param("now") Instant now);
}
