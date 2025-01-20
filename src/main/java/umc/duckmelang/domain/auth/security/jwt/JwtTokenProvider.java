package umc.duckmelang.domain.auth.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.error.exception.TokenException;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

@Component
@EnableWebSecurity
public class JwtTokenProvider {
    private final Key key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtTokenProvider(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.access-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-expiration}") long refreshTokenExpiration
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    /* JWT 토큰 생성 메소드 */
    public String createToken(Long memberId, long expiration) {
        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessToken(Long memberId) {
        return createToken(memberId, accessTokenExpiration);
    }

    public String generateRefreshToken(Long memberId) {
        return createToken(memberId, refreshTokenExpiration);
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


    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Long getMemberIdFromToken(String token) {
        Claims claims = parseClaims(token);
        return Long.valueOf(claims.getSubject());
    }

    public Authentication getAuthentication(String token){
        Long memberId = getMemberIdFromToken(token);
        return new UsernamePasswordAuthenticationToken(memberId, null, Collections.emptyList());
    }
}
