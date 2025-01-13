package umc.duckmelang.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;
    private final String secretKey = "temporarySecretKey1234567890temporarySecretKey1234567890"; // 임시 키
    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // JWT 토큰의 만료 시간 설정
    private static final long ACCESS_TOKEN_VALIDITY = 1000L * 60 * 30; // 30분
    private static final long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 7; // 7일

    // JWT 생성
    public JwtToken generateToken(Authentication authentication) {

        String userId = extractUserId(authentication);
        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_VALIDITY);
        String accessToken = Jwts.builder()
                .setSubject(userId) // 사용자 ID
                .setIssuedAt(new Date(now)) // 토큰 발급 시간
                .setExpiration(accessTokenExpiresIn) // 토큰 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 서명
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + REFRESH_TOKEN_VALIDITY))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // JWT 토큰에서 사용자 ID 추출
    public String getUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // Authentication 객체에서 사용자 ID 추출
    private String extractUserId(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername(); // 이메일 반환
        }
        throw new IllegalArgumentException("Authentication 객체에서 사용자 정보를 찾을 수 없습니다.");
    }

    // JWT 토큰의 유효성을 검증하는 로직
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            log.error("Token validation failed: {}", e.getMessage());
        }
        return false;
    }

    //  UsernamePasswordAuthenticationToken 인증 객체를 생성하여 SecurityContext에 저장한다
    public Authentication getAuthentication(String userId){
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
