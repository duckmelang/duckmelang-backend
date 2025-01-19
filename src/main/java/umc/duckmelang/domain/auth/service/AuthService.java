package umc.duckmelang.domain.auth.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.auth.dto.AuthResponseDto;
import umc.duckmelang.domain.refreshtoken.domain.RefreshToken;
import umc.duckmelang.domain.refreshtoken.service.RefreshTokenService;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.common.jwt.JwtUtil;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public AuthResponseDto.TokenResponse login(String email, String password){
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long memberId = userDetails.getMemberId();
            String emailFromDetails = userDetails.getUsername();
            AuthResponseDto.TokenResponse tokens = jwtUtil.generateToken(memberId, emailFromDetails);
            refreshTokenService.saveRefreshToken(tokens.getRefreshToken(), memberId);
            return tokens;
        } catch(UsernameNotFoundException | BadCredentialsException e){
            throw e;
        } 
    }

    @Transactional
    public AuthResponseDto.TokenResponse reissue(String refreshToken) {
        RefreshToken storedToken = refreshTokenService.validateAndGetRefreshToken(refreshToken);
        refreshTokenService.removeRefreshToken(refreshToken);
        Long memberId = storedToken.getMemberId();
        AuthResponseDto.TokenResponse newToken = jwtUtil.generateToken(memberId, storedToken.getToken());
        refreshTokenService.saveRefreshToken(newToken.getRefreshToken(), memberId);
        return newToken;
    }

    @Transactional
    public void logout(String accessToken) {
        refreshTokenService.removeRefreshToken(accessToken);
        SecurityContextHolder.clearContext();
    }
}
