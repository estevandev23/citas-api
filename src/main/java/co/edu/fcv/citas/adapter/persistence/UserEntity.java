package co.edu.fcv.citas.adapter.persistence;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "app_user")
class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @Column(name = "first_name", nullable = false) String firstName;
    @Column(name = "last_name", nullable = false) String lastName;
    @Column(name = "document_type", nullable = false) String documentType;
    @Column(name = "document_number", nullable = false) String documentNumber;
    @Column(nullable = false) String email;
    @Column(nullable = false) String phone;
    @Column(name = "password_hash", nullable = false) String passwordHash;
    @Column(nullable = false) boolean active;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role_code") Set<String> roles = new HashSet<>();
}
