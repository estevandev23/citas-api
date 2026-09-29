package co.edu.fcv.citas.adapter.http;

import co.edu.fcv.citas.application.ProfileService;
import co.edu.fcv.citas.application.port.UserProfiles;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/api/v1/profile") @PreAuthorize("hasRole('USER')")
class ProfileController {
    private final TransactionalProfileFacade service;
    ProfileController(TransactionalProfileFacade service) { this.service=service; }
    @GetMapping("/me") ProfileResponse mine(JwtAuthenticationToken auth) { return response(service.mine(id(auth))); }
    @PutMapping("/me") ProfileResponse update(JwtAuthenticationToken auth, @Valid @RequestBody UpdateRequest request) {
        return response(service.update(id(auth), new ProfileService.Update(request.firstName(),request.lastName(),request.phone(),request.insurerCode(),request.planCode(),request.regimeCode())));
    }
    private static Long id(JwtAuthenticationToken auth) { return Long.parseLong(auth.getToken().getSubject()); }
    private static ProfileResponse response(UserProfiles.Profile p) { return new ProfileResponse(p.userId(),p.firstName(),p.lastName(),p.documentType(),p.documentNumber(),p.email(),p.phone(),p.insurerCode(),p.insurerLabel(),p.planCode(),p.planLabel(),p.regimeCode(),p.regimeLabel()); }
    record UpdateRequest(@NotBlank String firstName,@NotBlank String lastName,@NotBlank String phone,@NotBlank String insurerCode,@NotBlank String planCode,@NotBlank String regimeCode) {}
    record ProfileResponse(Long userId,String firstName,String lastName,String documentType,String documentNumber,String email,String phone,String insurerCode,String insurerLabel,String planCode,String planLabel,String regimeCode,String regimeLabel) {}
}
