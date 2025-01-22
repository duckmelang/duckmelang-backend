package umc.duckmelang.global.security.filter;

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
import umc.duckmelang.domain.auth.service.AuthService;
import umc.duckmelang.global.apipayload.ApiResponse;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.TokenException;
import umc.duckmelang.global.redis.blacklist.BlacklistServiceImpl;
import umc.duckmelang.global.security.jwt.JwtTokenProvider;
import umc.duckmelang.global.security.jwt.JwtUtil;

import java.io.IOException;

/**
 * JWT 인증 필터
 *  요청 헤더에 포함된 JWT 토큰을 검증하고 인증 정보를 설정
 */
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final BlacklistServiceImpl blacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        try {
            String token = jwtUtil.extractToken(request);
            if (StringUtils.hasText(token) && jwtUtil.validateToken(token)) {
                if (blacklistService.isTokenBlacklisted(token)) {
                    throw new TokenException(ErrorStatus.INVALID_TOKEN);
                }
                Authentication authentication = jwtUtil.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            chain.doFilter(request, response);
        } catch (TokenException e) {
            handleTokenException(response, e);
        }
    }

    /**
     * 토큰 관련 예외를 처리하는 함수
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
