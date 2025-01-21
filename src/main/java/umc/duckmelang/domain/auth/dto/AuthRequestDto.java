package umc.duckmelang.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class AuthRequestDto {

    @Getter
    public static class LoginDto{
        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        private String email;
        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        private String password;
    }

    @Getter
    public static class RefreshTokenRequestDto{
        @NotNull
        private String refreshToken;
    }

    @Getter
    public static class LogoutDto {
        private String accessToken;
        private String refreshToken;
    }
}
