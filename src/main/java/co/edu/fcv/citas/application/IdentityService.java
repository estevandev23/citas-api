package co.edu.fcv.citas.application;

import co.edu.fcv.citas.application.port.PasswordService;
import co.edu.fcv.citas.application.port.RefreshSessions;
import co.edu.fcv.citas.application.port.TokenService;
import co.edu.fcv.citas.application.port.UserAccounts;
import co.edu.fcv.citas.domain.UserAccount;
import java.time.Clock;
import java.util.Locale;
import java.util.Set;

public class IdentityService {
    private final UserAccounts users;
    private final PasswordService passwords;
    private final TokenService tokens;
    private final RefreshSessions sessions;
    private final Clock clock;

    public IdentityService(UserAccounts users, PasswordService passwords, TokenService tokens,
                           RefreshSessions sessions, Clock clock) {
        this.users = users; this.passwords = passwords; this.tokens = tokens;
        this.sessions = sessions; this.clock = clock;
    }

    public UserAccount register(Registration request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        String type = request.documentType().trim().toUpperCase(Locale.ROOT);
        String number = request.documentNumber().trim();
        if (users.emailExists(email)) throw new IdentityException("EMAIL_EXISTS", "El email ya está registrado");
        if (users.documentExists(type, number)) throw new IdentityException("DOCUMENT_EXISTS", "El documento ya está registrado");
        return users.saveUser(new UserAccount(null, request.firstName().trim(), request.lastName().trim(),
                type, number, email, request.phone().trim(), passwords.hash(request.password()), true, Set.of("USER")));
    }

    public TokenService.IssuedTokens login(String email, String password) {
        var user = users.findByEmail(email.trim().toLowerCase(Locale.ROOT))
                .orElseThrow(this::badCredentials);
        if (!user.active() || !passwords.matches(password, user.passwordHash())) throw badCredentials();
        var issued = tokens.issue(user);
        sessions.save(user.id(), tokens.hashId(issued.refreshId()), issued.refreshExpiry());
        return issued;
    }

    public TokenService.IssuedTokens refresh(String refreshToken) {
        var identity = tokens.validateRefresh(refreshToken);
        if (!sessions.consume(identity.userId(), tokens.hashId(identity.tokenId()), clock.instant()))
            throw new IdentityException("INVALID_REFRESH", "Sesión no válida");
        var user = users.findById(identity.userId()).orElseThrow(this::invalidRefresh);
        if (!user.active()) throw invalidRefresh();
        var issued = tokens.issue(user);
        sessions.save(user.id(), tokens.hashId(issued.refreshId()), issued.refreshExpiry());
        return issued;
    }

    private IdentityException badCredentials() { return new IdentityException("INVALID_CREDENTIALS", "Credenciales inválidas"); }
    private IdentityException invalidRefresh() { return new IdentityException("INVALID_REFRESH", "Sesión no válida"); }

    public record Registration(String firstName, String lastName, String documentType,
                               String documentNumber, String email, String phone, String password) {}
}
