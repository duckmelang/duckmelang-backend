package umc.duckmelang.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;


import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // HTTP 요청의 헤더에서 JWT 추출
        String token = resolveToken((HttpServletRequest) request);
        System.out.println("Authorization Header in Filter: " + token);

        // 추출된 토큰이 유효한 경우, 인증 객체를 생성하고 SecurityContext에 저장
        if(token!=null && jwtTokenProvider.validateToken(token)){
            String userId = jwtTokenProvider.getUserId(token); // 토큰에서 사용자 ID 추출
            Authentication authentication = jwtTokenProvider.getAuthentication(userId); // 인증 정보 생성
            SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContext에 설정
            System.out.println("Authentication successful for user: " + userId);
        }

        // 다음 필터로 요청 전달
        chain.doFilter(request, response);
    }

    // HTTP 요청 헤더에서 JWT 토큰 추출
    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization"); // Authorization 헤더에서 토큰 추출
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7); // "Bearer " 제거 후 반환
        }
        return null;
    }
}
