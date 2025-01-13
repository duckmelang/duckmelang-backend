package umc.duckmelang.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class MemberRequestDto {

    @Getter
    public static class JoinDto{
        @NotBlank
        private String loginId;
        @NotBlank
        private String password;
    }
}
