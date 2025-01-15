package umc.duckmelang.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class AuthRequestDto {

    @Getter
    public static class LoginDto{
        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        private String email;
        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        private String password;
    }
}
