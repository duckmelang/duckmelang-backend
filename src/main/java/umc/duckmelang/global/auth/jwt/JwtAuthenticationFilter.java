package umc.duckmelang.global.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(AUTH_HEADER);

        // JWT가 포함되지 않은 요청 처리
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        // JWT 추출
        String token = authorizationHeader.substring(7); // "Bearer " 제거
        TokenStatus tokenStatus = jwtService.validateToken(token); // 토큰 상태 확인

        if (tokenStatus == TokenStatus.AUTHENTICATED) {
            // 유효한 토큰인 경우 사용자 정보 설정
            Long memberId = jwtService.getMemberIdFromToken(token);

            // SecurityContext에 인증 정보 설정
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(memberId, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } else if (tokenStatus == TokenStatus.EXPIRED) {
            // 만료된 토큰 처리
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token has expired");
            return;

        } else if (tokenStatus == TokenStatus.INVALID) {
            // 잘못된 토큰 처리
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token");
            return;
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}
