package umc.duckmelang.global.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.member.repository.MemberRepository;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JwtService {

    private final MemberRepository memberRepository;
    private final CustomMemberDetailsService customMemberDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpiration;

    /**
     * (1) Access Token 발급
     *     사용자 ID를 subject에 설정
     */
    public String createAccessToken(MemberPrincipal memberPrincipal) {
        long now = System.currentTimeMillis();
        Date nowDate = new Date(now);

        return Jwts.builder()
                .setSubject(String.valueOf(memberPrincipal.getMember().getId()))
                .setIssuedAt(nowDate)
                .setExpiration(new Date(now + accessTokenExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * (2) Refresh Token 생성
     *      사용자 ID를 subject에 설정
     */
    @Transactional
    public String createRefreshToken(MemberPrincipal memberPrincipal) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setSubject(String.valueOf(memberPrincipal.getMember().getId()))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + refreshTokenExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * (3) 토큰 검증
     *      TokenStatus 리턴
     */
    public TokenStatus validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token);
            return TokenStatus.AUTHENTICATED;
        } catch (ExpiredJwtException e) {
            return TokenStatus.EXPIRED;
        } catch (JwtException e) {
            return TokenStatus.INVALID;
        }
    }

    /**
     * (4) 토큰에서 사용자 ID 추출
     */
    public Long getMemberIdFromToken(String token) {
        String subject = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return Long.valueOf(subject);
    }

    // JWT 서명용 Key
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
