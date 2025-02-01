package umc.duckmelang.global.redis.refreshtoken;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.TokenException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    // RefreshToken 저장
    @Transactional
    @Override
    public void saveRefreshToken(String refreshToken, Long memberId) {
        refreshTokenRepository.findByMemberId(memberId)
                .ifPresent(token -> refreshTokenRepository.deleteById(token.getToken()));
        RefreshToken token = RefreshToken.builder()
                .token(refreshToken)
                .memberId(memberId)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();
        refreshTokenRepository.save(token);
    }

    // RefreshToken 유효성 검증 및 조회
    @Transactional(readOnly = true)
    @Override
    public RefreshToken validateAndGetRefreshToken(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new TokenException(ErrorStatus.MISSING_TOKEN));
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenException(ErrorStatus.TOKEN_EXPIRED);
        }
        return token;
    }

    // RefreshToken 삭제
    @Transactional
    @Override
    public void removeRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteById(refreshToken);
    }
}
