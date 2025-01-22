package umc.duckmelang.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.TokenException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * RefreshToken 저장
     * @param refreshToken 저장할 RefreshToken
     * @param memberId 회원 ID
     */
    @Transactional
    public void saveRefreshToken(String refreshToken, Long memberId){
        StreamSupport.stream(refreshTokenRepository.findAll().spliterator(), false)
                .filter(token -> token.getMemberId().equals(memberId))
                .forEach(token -> refreshTokenRepository.deleteById(token.getToken()));
        if (refreshTokenRepository.existsById("refreshToken")) {
            System.out.println("Deleting unnecessary 'refreshToken' key");
            refreshTokenRepository.deleteById("refreshToken");
        }
        RefreshToken token = RefreshToken.builder()
                .token(refreshToken)
                .memberId(memberId)
                .build();
        refreshTokenRepository.save(token);
    }

    /**
     * RefreshToken 유효성 검증 및 조회
     * @param refreshToken 조회할 RefreshToken
     * @return 유효한 RefreshToken 객체
     */
    @Transactional(readOnly = true)
    public RefreshToken validateAndGetRefreshToken(String refreshToken) {
        Optional<RefreshToken> storedToken = refreshTokenRepository.findById(refreshToken);
        if (storedToken.isEmpty()) {
            throw new TokenException(ErrorStatus.MISSING_TOKEN);
        }
        RefreshToken token = storedToken.get();
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenException(ErrorStatus.TOKEN_EXPIRED);
        }
        return storedToken.get();
    }

    /**
     * RefreshToken 삭제
     * @param refreshToken 삭제할 RefreshToken
     */
    @Transactional
    public void removeRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteById(refreshToken);
    }
}
