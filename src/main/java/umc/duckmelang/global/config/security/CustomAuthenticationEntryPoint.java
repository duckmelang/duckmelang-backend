package umc.duckmelang.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
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
        String code = "COMMON401";

        // 요청 attribute에 토큰 상태가 있는 경우 메시지 변경
        if (request.getAttribute("tokenError") != null) {
            ErrorStatus errorStatus = (ErrorStatus) request.getAttribute("tokenError");
            message = errorStatus.getMessage();
            code = errorStatus.getCode();
        }

        ApiResponse<Object> errorResponse = ApiResponse.onFailure(code, message, null);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }
}
