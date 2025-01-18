package umc.duckmelang.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import umc.duckmelang.global.apipayload.ApiResponse;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        // 기본값 설정
        ErrorStatus errorStatus = ErrorStatus.AUTH400;

        if (request.getAttribute("tokenError") != null) {
            errorStatus = ErrorStatus.valueOf((String) request.getAttribute("tokenError"));
        } else if (authException instanceof UsernameNotFoundException) {
            errorStatus = ErrorStatus.AUTH404;
        } else if (authException instanceof BadCredentialsException) {
            errorStatus = ErrorStatus.AUTH401;
        }

        ApiResponse<Object> errorResponse = ApiResponse.onFailure(
                errorStatus.getCode(),
                errorStatus.getMessage(),
                null
        );

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }
}
