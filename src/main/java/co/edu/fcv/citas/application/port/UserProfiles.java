package co.edu.fcv.citas.application.port;

import java.util.Optional;

public interface UserProfiles {
    Optional<Profile> find(Long userId);
    boolean validAffiliation(String insurerCode, String planCode, String regimeCode);
    void update(Long userId, String firstName, String lastName, String phone,
                String insurerCode, String planCode, String regimeCode);

    record Profile(Long userId, String firstName, String lastName, String documentType,
                   String documentNumber, String email, String phone, String insurerCode,
                   String insurerLabel, String planCode, String planLabel, String regimeCode,
                   String regimeLabel) {}
}
