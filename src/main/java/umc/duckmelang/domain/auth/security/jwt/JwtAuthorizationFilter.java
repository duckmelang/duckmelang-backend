package umc.duckmelang.domain.auth.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.error.exception.TokenException;

import java.io.IOException;

/**
 * JWT 인증 필터
 * 클라이언트 요청 헤더에 포함된 JWT 토큰을 검증하고 인증 정보를 설정한다.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter  {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return  path.startsWith("/login") || path.startsWith("/signup") || path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        try {
            String token = extractToken(request);
            if (jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException e) {
            request.setAttribute("tokenError", ErrorStatus.TOKEN_EXPIRED);
        } catch (JwtException e) {
            request.setAttribute("tokenError", ErrorStatus.INVALID_TOKEN);
        }
        chain.doFilter(request, response);
    }


    /**
     * Authorization 헤더에서 JWT 토큰을 추출합니다.
     * @param request 클라이언트 요청 객체
     * @return JWT 토큰 -> Bearer 제거 후 반환
     */
    private String extractToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new TokenException(ErrorStatus.INVALID_TOKEN);
    }
}
