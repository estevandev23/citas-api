package co.edu.fcv.citas.adapter.http;

import co.edu.fcv.citas.application.IdentityService;
import co.edu.fcv.citas.application.port.TokenService;
import co.edu.fcv.citas.domain.UserAccount;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
class AuthController {
    private final TransactionalIdentityFacade service;
    AuthController(TransactionalIdentityFacade service) { this.service = service; }

    @PostMapping("/auth/register")
    ResponseEntity<RegisteredUser> register(@Valid @RequestBody RegisterRequest request) {
        UserAccount user = service.register(new IdentityService.Registration(request.firstName(), request.lastName(),
                request.documentType(), request.documentNumber(), request.email(), request.phone(), request.password()));
        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisteredUser(user.id(), user.firstName(),
                user.lastName(), user.email(), user.roles()));
    }

    @PostMapping("/auth/login")
    TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return response(service.login(request.email(), request.password()));
    }

    @PostMapping("/auth/refresh")
    TokenResponse refresh(@Valid @RequestBody RefreshRequest request) {
        return response(service.refresh(request.refreshToken()));
    }

    @GetMapping("/session/me")
    @PreAuthorize("hasAnyRole('USER', 'PROFESSIONAL', 'ADMIN')")
    SessionView me(JwtAuthenticationToken auth) {
        return new SessionView(Long.parseLong(auth.getToken().getSubject()),
                Set.copyOf(auth.getToken().getClaimAsStringList("roles")));
    }

    private TokenResponse response(TokenService.IssuedTokens tokens) {
        return new TokenResponse(tokens.accessToken(), tokens.refreshToken(), "Bearer");
    }

    record RegisterRequest(@NotBlank String firstName, @NotBlank String lastName,
                           @NotBlank String documentType, @NotBlank String documentNumber,
                           @NotBlank @Email String email, @NotBlank String phone,
                           @NotBlank String password) {}
    record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {}
    record RefreshRequest(@NotBlank String refreshToken) {}
    record RegisteredUser(Long id, String firstName, String lastName, String email, Set<String> roles) {}
    record TokenResponse(String accessToken, String refreshToken, String tokenType) {}
    record SessionView(Long userId, Set<String> roles) {}
}
