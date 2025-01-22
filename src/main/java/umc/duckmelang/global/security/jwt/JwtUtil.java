package umc.duckmelang.global.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.TokenException;
import umc.duckmelang.global.redis.blacklist.BlacklistService;

import java.util.Collections;
import java.util.Date;

/**
 * JWT 유효성 및 블랙리스트 체크
 */
@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtTokenProvider jwtTokenProvider;
    private final BlacklistService blacklistService;

    // 토큰의 유효성 검증
    public boolean validateToken(String token) {
        if (blacklistService.isTokenBlacklisted(token)) {
            throw new TokenException(ErrorStatus.INVALID_TOKEN);
        }
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtTokenProvider.getKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new TokenException(ErrorStatus.TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new TokenException(ErrorStatus.INVALID_TOKEN);
        }
    }

    //토큰의 만료 시간 확인
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = jwtTokenProvider.parseClaims(token).getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    //토큰의 남은 유효시간 계산
    public long getExpirationFromToken(String token) {
        try {
            Date expiration = jwtTokenProvider.parseClaims(token).getExpiration();
            return expiration.getTime() - System.currentTimeMillis();
        } catch (JwtException e) {
            throw new TokenException(ErrorStatus.INVALID_TOKEN);
        }
    }

    // JWT 토큰으로부터 Authentication 객체 생성
    public Authentication getAuthentication(String token) {
        Long memberId = jwtTokenProvider.getMemberIdFromToken(token);
        return new UsernamePasswordAuthenticationToken(memberId, null, Collections.emptyList());
    }
}
