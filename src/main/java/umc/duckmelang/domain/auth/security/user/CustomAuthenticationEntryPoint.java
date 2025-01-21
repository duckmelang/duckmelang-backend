package umc.duckmelang.domain.auth.security.user;

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
import umc.duckmelang.global.apipayload.code.BaseErrorCode;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.error.exception.TokenException;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        // 기본 예외 상태
        ErrorStatus errorStatus = ErrorStatus.AUTH_UNAUTHORIZED;

        BaseErrorCode tokenError = (BaseErrorCode) request.getAttribute("tokenError");
        if (tokenError != null) {
            errorStatus = (ErrorStatus) tokenError;
        }

        if (authException instanceof UsernameNotFoundException) {
            errorStatus = ErrorStatus.AUTH_USER_NOT_FOUND;
        } else if (authException instanceof BadCredentialsException) {
            errorStatus = ErrorStatus.AUTH_INVALID_CREDENTIALS;
        }
        ApiResponse<Object> errorResponse = ApiResponse.onFailure(
                errorStatus.getCode(),
                errorStatus.getMessage(),
                null
        );

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorStatus.getHttpStatus().value());
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }
}
