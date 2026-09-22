package co.edu.fcv.citas.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface JpaUsers extends JpaRepository<UserEntity, Long> {
    java.util.Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByDocumentTypeAndDocumentNumber(String type, String number);
}
