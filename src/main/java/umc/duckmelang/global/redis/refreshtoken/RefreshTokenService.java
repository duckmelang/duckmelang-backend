package umc.duckmelang.global.redis.refreshtoken;

import org.springframework.stereotype.Service;

@Service
public interface RefreshTokenService {
    void saveRefreshToken(String refreshToken, Long memberId);
    RefreshToken validateAndGetRefreshToken(String refreshToken);
    void removeRefreshToken(String refreshToken);
}
