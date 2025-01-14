package umc.duckmelang.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class MemberRequestDto {

    @Getter
    public static class SignupDto{
        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        @Email(message = "올바른 이메일 형식이어야 합니다.")
        private String email;
        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        private String password;
    }

    @Getter
    public static class LoginDto{
        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        private String email;
        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        private String password;
    }

    @Getter
    public static class RefreshDto{
        private String refreshToken;
    }
}
