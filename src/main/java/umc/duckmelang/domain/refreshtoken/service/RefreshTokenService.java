package umc.duckmelang.domain.refreshtoken.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.duckmelang.domain.refreshtoken.domain.RefreshToken;
import umc.duckmelang.domain.refreshtoken.repository.RefreshTokenRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveRefreshToken(String refreshToken, Long memberId){
        String key = "refreshToken:" + refreshToken;
        RefreshToken token = RefreshToken.builder()
                .id(key)
                .memberId(memberId)
                .token(refreshToken)
                .build();
        refreshTokenRepository.save(token);
    }

    @Transactional(readOnly = true)
    public RefreshToken validateAndGetRefreshToken(String refreshToken) {
        String key = "refreshToken:" + refreshToken;
        Optional<RefreshToken> storedToken = refreshTokenRepository.findById(key);
        if (storedToken.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        return storedToken.get();
    }

    @Transactional
    public void removeRefreshToken(String refreshToken){
        String key = "refreshToken:" + refreshToken;
        refreshTokenRepository.deleteById(key);
    }
}
