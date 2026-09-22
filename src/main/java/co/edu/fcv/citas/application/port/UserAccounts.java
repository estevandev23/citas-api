package co.edu.fcv.citas.application.port;

import co.edu.fcv.citas.domain.UserAccount;
import java.util.Optional;

public interface UserAccounts {
    Optional<UserAccount> findByEmail(String email);
    Optional<UserAccount> findById(Long id);
    boolean emailExists(String email);
    boolean documentExists(String type, String number);
    UserAccount saveUser(UserAccount user);
}
