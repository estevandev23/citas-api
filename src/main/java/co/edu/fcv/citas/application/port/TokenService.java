package co.edu.fcv.citas.application.port;

import co.edu.fcv.citas.domain.UserAccount;
import java.time.Instant;

public interface TokenService {
    IssuedTokens issue(UserAccount user);
    RefreshIdentity validateRefresh(String token);
    String hashId(String tokenId);

    record IssuedTokens(String accessToken, String refreshToken, Instant refreshExpiry, String refreshId) {}
    record RefreshIdentity(Long userId, String tokenId) {}
}
