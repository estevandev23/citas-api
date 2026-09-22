package co.edu.fcv.citas.adapter.persistence;

import co.edu.fcv.citas.application.IdentityException;
import co.edu.fcv.citas.application.port.UserAccounts;
import co.edu.fcv.citas.domain.UserAccount;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
class UserPersistenceAdapter implements UserAccounts {
    private final JpaUsers repo;
    UserPersistenceAdapter(JpaUsers repo) { this.repo = repo; }
    public Optional<UserAccount> findByEmail(String email) { return repo.findByEmail(email).map(this::map); }
    public Optional<UserAccount> findById(Long id) { return repo.findById(id).map(this::map); }
    public boolean emailExists(String email) { return repo.existsByEmail(email); }
    public boolean documentExists(String type, String number) { return repo.existsByDocumentTypeAndDocumentNumber(type, number); }
    public UserAccount saveUser(UserAccount user) {
        var entity = new UserEntity();
        entity.firstName = user.firstName(); entity.lastName = user.lastName();
        entity.documentType = user.documentType(); entity.documentNumber = user.documentNumber();
        entity.email = user.email(); entity.phone = user.phone(); entity.passwordHash = user.passwordHash();
        entity.active = user.active(); entity.roles.addAll(user.roles());
        try { return map(repo.saveAndFlush(entity)); }
        catch (DataIntegrityViolationException ex) { throw new IdentityException("IDENTITY_EXISTS", "Email o documento ya registrado"); }
    }
    private UserAccount map(UserEntity u) {
        return new UserAccount(u.id, u.firstName, u.lastName, u.documentType, u.documentNumber,
                u.email, u.phone, u.passwordHash, u.active, java.util.Set.copyOf(u.roles));
    }
}
