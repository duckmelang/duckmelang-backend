package umc.duckmelang.global.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.TokenException;
import umc.duckmelang.global.redis.blacklist.BlacklistService;
import umc.duckmelang.global.security.user.CustomUserDetails;

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
        CustomUserDetails userDetails = new CustomUserDetails(new Member(memberId));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    // 헤더에서 JWT 토큰을 추출
    public String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // SecurityContextHolder 에서 현재 로그인한 사용자의 JWT 가져오기
    public CustomUserDetails extractUserDetailsFromSecurityContext(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            throw new TokenException(ErrorStatus.INVALID_TOKEN);
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails userDetails){
            return userDetails;
        }else{
            throw new TokenException(ErrorStatus.INVALID_TOKEN);
        }
    }
}
