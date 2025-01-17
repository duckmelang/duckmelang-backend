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

        String message = "인증이 필요합니다.";
        String code = "AUTH400";

        if (request.getAttribute("tokenError") != null) {
            ErrorStatus tokenErrorStatus = (ErrorStatus) request.getAttribute("tokenError");
            message = tokenErrorStatus.getMessage();
            code = tokenErrorStatus.getCode();
        } else if (authException instanceof UsernameNotFoundException) {
            message = authException.getMessage();
            code = "AUTH404";
        } else if (authException instanceof BadCredentialsException) {
            message = "이메일 또는 비밀번호가 잘못되었습니다.";
            code = "AUTH401";
        }

        ApiResponse<Object> errorResponse = ApiResponse.onFailure(code, message, null);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }
}
