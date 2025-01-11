package umc.duckmelang.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final Key key;
    private final UserDetailsService userDetailsService;

    // 생성자를 통해 비밀 키 초기화
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes); // Key 객체 생성
    }

    // JWT 토큰의 만료 시간 설정
    private static final long ACCESS_TOKEN_VALIDITY = 1000L * 60 * 30; // 30분
    private static final long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 7; // 7일

    // Member 정보를 가지고 AccessToken, RefreshToken을 생성하는 메서드
    public JwtToken generateToken(Authentication authentication){

        String userId = extractUserId(authentication);
        long now = (new Date()).getTime();

        // Access Token 생성 (만료 시간 30분)
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_VALIDITY);
        String accessToken = Jwts.builder()
                .setSubject(userId) // 사용자 ID
                .setIssuedAt(new Date(now)) // 토큰 발급 시간
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성 (만료 시간 : 7일)
        String refreshToken = Jwts.builder()
                .setSubject(userId) // 사용자 식별자
                .setIssuedAt(new Date(now)) // 토큰 발급 시간
                .setExpiration(new Date(now + REFRESH_TOKEN_VALIDITY))
                .signWith(key, SignatureAlgorithm.HS256) // 서명
                .compact();

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // JWT 토큰에서 사용자 ID 추출
    public String getUserId(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject(); // 사용자 ID 반환
    }

    // Authentication 객체에서 사용자 ID 추출
    private String extractUserId(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername(); // 이메일 반환
        }
        throw new IllegalArgumentException("Authentication 객체에서 사용자 정보를 찾을 수 없습니다.");
    }

    // JWT 토큰 기반으로 인증 객체 (Authentication) 생성
    public Authentication getAuthentication(String token){
        // 토큰에서 사용자 ID 추출
        String userId = getUserId(token);
        // 사용자 ID를 기반으로 UserDetails 객체 생성
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        // Authentication 객체 반환
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities())

    }

    // JWT 토큰의 유효성을 검증하는 로직
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }
}
