package co.edu.fcv.citas.adapter.http;

import co.edu.fcv.citas.application.IdentityService;
import co.edu.fcv.citas.application.port.TokenService;
import co.edu.fcv.citas.domain.UserAccount;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.Duration;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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
    private final LocalRecoveryTokenExposure recoveryTokenExposure;
    private final long refreshDays;
    AuthController(TransactionalIdentityFacade service, LocalRecoveryTokenExposure recoveryTokenExposure,
                   @Value("${app.jwt.refresh-days:7}") long refreshDays) {
        this.service = service; this.recoveryTokenExposure = recoveryTokenExposure;
        this.refreshDays = refreshDays;
    }

    @PostMapping("/auth/register")
    ResponseEntity<RegisteredUser> register(@Valid @RequestBody RegisterRequest request) {
        UserAccount user = service.register(new IdentityService.Registration(request.firstName(), request.lastName(),
                request.documentType(), request.documentNumber(), request.email(), request.phone(), request.password()));
        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisteredUser(user.id(), user.firstName(),
                user.lastName(), user.email(), user.roles()));
    }

    @PostMapping("/auth/login")
    ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        var tokens = service.login(request.email(), request.password());
        return withRefreshCookie(response(tokens), tokens.refreshToken());
    }

    @PostMapping("/auth/refresh")
    ResponseEntity<TokenResponse> refresh(@Valid @RequestBody(required = false) RefreshRequest request,
                                          @org.springframework.web.bind.annotation.CookieValue(name = REFRESH_COOKIE, required = false) String cookie) {
        String token = request != null && request.refreshToken() != null && !request.refreshToken().isBlank()
                ? request.refreshToken() : cookie;
        var tokens = service.refresh(token == null ? "" : token);
        return withRefreshCookie(response(tokens), tokens.refreshToken());
    }

    @PostMapping("/auth/logout")
    ResponseEntity<Void> logout(@Valid @RequestBody(required = false) RefreshRequest request,
                                @org.springframework.web.bind.annotation.CookieValue(name = REFRESH_COOKIE, required = false) String cookie) {
        String token = request != null && request.refreshToken() != null && !request.refreshToken().isBlank()
                ? request.refreshToken() : cookie;
        service.logout(token == null ? "" : token);
        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, clearRefreshCookie().toString()).build();
    }

    @PostMapping("/auth/password-recovery")
    ResponseEntity<?> passwordRecovery(@Valid @RequestBody PasswordRecoveryRequest request) {
        var token = service.requestPasswordRecovery(request.email());
        if (recoveryTokenExposure.enabled() && token.isPresent())
            return ResponseEntity.accepted().body(new RecoveryResponse(token.get()));
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/auth/password-reset")
    ResponseEntity<Void> passwordReset(@Valid @RequestBody PasswordResetRequest request) {
        service.resetPassword(request.token(), request.newPassword());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/session/me")
    @PreAuthorize("hasAnyRole('USER', 'PROFESSIONAL', 'ADMIN')")
    SessionView me(JwtAuthenticationToken auth) {
        return new SessionView(Long.parseLong(auth.getToken().getSubject()),
                Set.copyOf(auth.getToken().getClaimAsStringList("roles")));
    }

    private static final String REFRESH_COOKIE = "citas_refresh";

    private TokenResponse response(TokenService.IssuedTokens tokens) {
        return new TokenResponse(tokens.accessToken(), tokens.refreshToken(), "Bearer");
    }

    private ResponseEntity<TokenResponse> withRefreshCookie(TokenResponse response, String refreshToken) {
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, refreshCookie(refreshToken).toString()).body(response);
    }

    private ResponseCookie refreshCookie(String token) {
        return ResponseCookie.from(REFRESH_COOKIE, token).httpOnly(true).secure(false).sameSite("Lax")
                .path("/api/v1/auth").maxAge(Duration.ofDays(refreshDays)).build();
    }

    private ResponseCookie clearRefreshCookie() {
        return ResponseCookie.from(REFRESH_COOKIE, "").httpOnly(true).secure(false).sameSite("Lax")
                .path("/api/v1/auth").maxAge(Duration.ZERO).build();
    }

    record RegisterRequest(@NotBlank String firstName, @NotBlank String lastName,
                           @NotBlank String documentType, @NotBlank String documentNumber,
                           @NotBlank @Email String email, @NotBlank String phone,
                           @NotBlank String password) {}
    record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {}
    record RefreshRequest(@NotBlank String refreshToken) {}
    record PasswordRecoveryRequest(@NotBlank @Email String email) {}
    record PasswordResetRequest(@NotBlank String token, @NotBlank String newPassword) {}
    record RegisteredUser(Long id, String firstName, String lastName, String email, Set<String> roles) {}
    record TokenResponse(String accessToken, String refreshToken, String tokenType) {}
    record RecoveryResponse(String resetToken) {}
    record SessionView(Long userId, Set<String> roles) {}
}
