package co.edu.fcv.citas.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface JpaUsers extends JpaRepository<UserEntity, Long> {
    java.util.Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByDocumentTypeAndDocumentNumber(String type, String number);

    @Modifying
    @Query("update UserEntity u set u.passwordHash = :passwordHash where u.id = :userId")
    int updatePassword(@Param("userId") Long userId, @Param("passwordHash") String passwordHash);
}
