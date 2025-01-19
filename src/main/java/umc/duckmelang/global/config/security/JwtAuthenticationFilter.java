package umc.duckmelang.global.config.security;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.common.jwt.JwtUtil;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = resolveToken(httpRequest);

        if (token != null) {
            if(isTokenBlacklisted(token)){
                String isBlacklisted = redisTemplate.opsForValue().get("blacklist:" + token);
                if (isBlacklisted != null) {
                    throw new IllegalArgumentException("블랙리스트에 등록된 토큰입니다.");
                }
            }
            try {
                jwtUtil.validateToken(token);
                String email = jwtUtil.getEmailFromToken(token);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (ExpiredJwtException e) {
                httpRequest.setAttribute("tokenError", ErrorStatus.TOKEN_EXPIRED);
            } catch (JwtException e) {
                httpRequest.setAttribute("tokenError", ErrorStatus.INVALID_TOKEN);
            }
        } else {
            httpRequest.setAttribute("tokenError", ErrorStatus.TOKEN_NOT_FOUND);
        }
        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean isTokenBlacklisted(String token) {
        Boolean isBlacklisted = redisTemplate.hasKey("blacklist:" + token);
        return isBlacklisted != null && isBlacklisted;
    }
}

