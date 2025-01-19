package umc.duckmelang.global.common.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Component;
import umc.duckmelang.domain.auth.dto.AuthResponseDto;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;

import java.security.Key;
import java.util.Date;

@Component
@EnableWebSecurity
public class JwtUtil {
    private final Key key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtUtil(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.access-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-expiration}") long refreshTokenExpiration
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public AuthResponseDto.TokenResponse generateToken(Long memberId, String email) {
        String accessToken = createToken(memberId, email, accessTokenExpiration);
        String refreshToken = createToken(memberId, email, refreshTokenExpiration);

        return AuthResponseDto.TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String createToken(Long memberId, String email, long expirationTime) {
        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (JwtException e) {
            throw e;
        }
    }

    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String getEmailFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.get("email", String.class);
    }

    public Long getMemberIdFromToken(String token) {
        Claims claims = parseClaims(token);
        System.out.println("Parsed Claims: " + claims);
        return Long.valueOf(claims.getSubject());
    }

    public Long getExpiration(String token) {
        Claims claims = parseClaims(token);
        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }
}
