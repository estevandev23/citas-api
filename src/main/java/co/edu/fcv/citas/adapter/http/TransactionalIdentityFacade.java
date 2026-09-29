package co.edu.fcv.citas.adapter.http;

import co.edu.fcv.citas.application.IdentityService;
import co.edu.fcv.citas.application.port.TokenService;
import co.edu.fcv.citas.domain.UserAccount;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class TransactionalIdentityFacade {
    private final IdentityService service;
    TransactionalIdentityFacade(IdentityService service) { this.service = service; }

    @Transactional
    public UserAccount register(IdentityService.Registration request) { return service.register(request); }

    @Transactional
    public TokenService.IssuedTokens login(String email, String password) { return service.login(email, password); }

    @Transactional
    public TokenService.IssuedTokens refresh(String token) { return service.refresh(token); }

    @Transactional
    public void logout(String refreshToken) { service.logout(refreshToken); }

    @Transactional
    public java.util.Optional<String> requestPasswordRecovery(String email) {
        return service.requestPasswordRecovery(email);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) { service.resetPassword(token, newPassword); }
}
