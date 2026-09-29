package co.edu.fcv.citas.application;

import co.edu.fcv.citas.application.port.UserProfiles;

public class ProfileService {
    private final UserProfiles profiles;
    public ProfileService(UserProfiles profiles) { this.profiles = profiles; }
    public UserProfiles.Profile mine(Long userId) { return profiles.find(userId).orElseThrow(() -> new ProfileException("Perfil no encontrado")); }
    public UserProfiles.Profile update(Long userId, Update request) {
        if (!profiles.validAffiliation(request.insurerCode(), request.planCode(), request.regimeCode()))
            throw new ProfileException("La afiliación no referencia un catálogo activo");
        profiles.update(userId, request.firstName().trim(), request.lastName().trim(), request.phone().trim(),
                request.insurerCode(), request.planCode(), request.regimeCode());
        return mine(userId);
    }
    public record Update(String firstName, String lastName, String phone, String insurerCode, String planCode, String regimeCode) {}
}
