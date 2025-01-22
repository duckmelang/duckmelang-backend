package umc.duckmelang.domain.auth.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.auth.dto.AuthResponseDto;
import umc.duckmelang.global.apipayload.exception.TokenException;
import umc.duckmelang.global.redis.RefreshToken;
import umc.duckmelang.global.redis.RefreshTokenService;
import umc.duckmelang.global.security.jwt.JwtTokenProvider;
import umc.duckmelang.global.security.user.CustomUserDetails;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.AuthException;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 사용자 로그인
     * @param email    이메일
     * @param password 비밀번호
     * @return AccessToken과 RefreshToken
     */
    @Transactional
    public AuthResponseDto.TokenResponse login(String email, String password){
        try{
            Authentication authentication = authenticate(email, password);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long memberId = userDetails.getMemberId();
            String accessToken = jwtTokenProvider.generateAccessToken(memberId);
            String refreshToken = jwtTokenProvider.generateRefreshToken(memberId);
            refreshTokenService.saveRefreshToken(refreshToken, memberId);
            return new AuthResponseDto.TokenResponse(accessToken, refreshToken);
        } catch (UsernameNotFoundException e) {
            throw new AuthException(ErrorStatus.AUTH_USER_NOT_FOUND);
        } catch (BadCredentialsException e) {
            throw new AuthException(ErrorStatus.AUTH_INVALID_CREDENTIALS);
        }
    }

    /**
     * 토큰 재발급
     * @param refreshToken 유효한 RefreshToken
     * @return 새로 생성된 AccessToken과 RefreshToken
     */
    @Transactional
    public AuthResponseDto.TokenResponse reissue(String refreshToken) {
        RefreshToken storedToken = refreshTokenService.validateAndGetRefreshToken(refreshToken);
        refreshTokenService.removeRefreshToken(refreshToken);
        Long memberId = storedToken.getMemberId();
        String newAccessToken = jwtTokenProvider.generateAccessToken(memberId);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(memberId);
        refreshTokenService.saveRefreshToken(newRefreshToken, memberId);
        return new AuthResponseDto.TokenResponse(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void logout(String accessToken) {
        if (jwtTokenProvider.isTokenExpired(accessToken)) {
            throw new TokenException(ErrorStatus.INVALID_TOKEN);
        }

        long expiration = jwtTokenProvider.getExpirationFromToken(accessToken);
        redisTemplate.opsForValue().set(
                "blacklist:accessToken:" + accessToken,
                "true",
                expiration,
                TimeUnit.MILLISECONDS
        );
    }

    /**
     * 사용자 인증
     * @param email    이메일
     * @param password 비밀번호
     * @return Authentication 객체
     */
    private Authentication authenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        return authenticationManager.authenticate(authenticationToken);
    }
}
