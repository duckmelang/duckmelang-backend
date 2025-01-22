package umc.duckmelang.global.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.TokenException;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

@Component
@EnableWebSecurity
public class JwtTokenProvider {
    private final Key key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    private final RedisTemplate<String, String> redisTemplate;

    public JwtTokenProvider(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.access-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-expiration}") long refreshTokenExpiration,
            RedisTemplate<String, String> redisTemplate
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.redisTemplate = redisTemplate;
    }

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
        // 블랙리스트 확인
        if (isTokenBlacklisted(token)) {
            throw new TokenException(ErrorStatus.INVALID_TOKEN);
        }

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new TokenException(ErrorStatus.TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new TokenException(ErrorStatus.INVALID_TOKEN);
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

    private boolean isTokenBlacklisted(String token) {
        return redisTemplate.hasKey("blacklist:accessToken:" + token);
    }

    public long getExpirationFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getExpiration().getTime() - System.currentTimeMillis(); // 남은 유효시간 반환
        } catch (JwtException e) {
            throw new TokenException(ErrorStatus.INVALID_TOKEN);
        }
    }

}
