package umc.duckmelang.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import umc.duckmelang.global.apipayload.ApiResponse;
import umc.duckmelang.global.apipayload.exception.TokenException;

import java.io.IOException;

/**
 * JWT 인증 필터
 * 클라이언트 요청 헤더에 포함된 JWT 토큰을 검증하고 인증 정보를 설정한다.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        try {
            String token = extractToken(request);
            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            chain.doFilter(request, response);
        } catch (TokenException e) {
            handleTokenException(response, e);
        }
    }

    /**
     * Authorization 헤더에서 JWT 토큰을 추출합니다.
     * @param request 클라이언트 요청 객체
     * @return JWT 토큰 -> Bearer 제거 후 반환
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 토큰 관련 예외를 처리하는 함수.
     * @param response 클라이언트 응답 객체
     * @param e 발생한 TokenException
     */
    private void handleTokenException(HttpServletResponse response, TokenException e) throws IOException {
        response.setStatus(e.getErrorCode().getReasonHttpStatus().getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        ApiResponse<Object> errorResponse = ApiResponse.onFailure(
                e.getErrorCode().getReasonHttpStatus().getCode(),
                e.getErrorCode().getReasonHttpStatus().getMessage(),
                null
        );
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }
}
