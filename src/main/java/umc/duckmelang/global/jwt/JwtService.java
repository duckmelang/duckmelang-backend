package umc.duckmelang.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.member.domain.Member;
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

    // yml(or properties) 설정에서 주입받는다고 가정
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpiration;

    /**
     * (1) Access Token 발급
     *     - Subject에 memberId(DB PK)를 담는다
     */
    public String createAccessToken(Long memberId) {
        long now = System.currentTimeMillis();
        Date nowDate = new Date(now);

        return Jwts.builder()
                .setSubject(String.valueOf(memberId))           // memberId
                .setIssuedAt(nowDate)                           // 발행 시각
                .setExpiration(new Date(now + accessTokenExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * (2) Refresh Token 발급 (DB에 저장)
     *     - 현재는 발급만 하고, 재발급 기능은 없음
     */
    @Transactional
    public String createRefreshToken(Long memberId) {
        long now = System.currentTimeMillis();
        String refreshToken = Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + refreshTokenExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        // DB에 저장
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found. ID=" + memberId));
        member.setRefreshToken(refreshToken);

        return refreshToken;
    }

    /**
     * (3) 토큰 검증 → TokenStatus 리턴
     */
    public TokenStatus validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token);  // 파싱 중 예외 없으면 OK
            return TokenStatus.AUTHENTICATED;
        } catch (ExpiredJwtException e) {
            return TokenStatus.EXPIRED;
        } catch (JwtException e) {
            return TokenStatus.INVALID;
        }
    }

    /**
     * (4) 토큰에서 memberId 추출
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

    /**
     * (5) SecurityContextHolder에 저장할 Authentication 생성
     */
    public Authentication getAuthentication(String token) {
        Long memberId = getMemberIdFromToken(token);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found. ID=" + memberId));

        // principal 을 email 로 로드
        UserDetails userDetails = customMemberDetailsService.loadUserByUsername(member.getEmail());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // JWT 서명용 Key
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
