package co.edu.fcv.citas.adapter.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "refresh_session")
class RefreshSessionEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @Column(name = "user_id", nullable = false) Long userId;
    @Column(name = "token_id_hash", nullable = false, length = 64, columnDefinition = "char(64)") String tokenIdHash;
    @Column(name = "expires_at", nullable = false) Instant expiresAt;
    @Column(name = "consumed_at") Instant consumedAt;
}
