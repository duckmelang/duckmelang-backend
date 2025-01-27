package umc.duckmelang.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class MemberRequestDto {

    @Getter
    public static class SignupDto{
        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        @Pattern(
                regexp = "^[0-9a-zA-Z._%+-]+@[0-9a-zA-Z.-]+\\.[a-zA-Z]{2,}$",
                message = "유효한 이메일 주소 형식이 아닙니다.")
        @Schema(description = "사용자의 이메일 주소", example = "test@example.com")
        private String email;

        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        private String password;
    }

    @Builder
    @Getter
    public static class SelectIdolsDto{
        @NotEmpty(message = "최소 하나의 아이돌을 선택해야 합니다.")
        @Size(min = 1, message = "최소 하나의 아이돌을 선택해야 합니다.")
        private List<Long> idolCategoryIds;
    }

    @Builder
    @Getter
    public static class SelectEventsDto{
        private List<Long> eventCategoryIds;
    }

    @Builder
    @Getter
    public static class CreateLandminesDto {
        private List<String> landmineContents;
    }

    @Builder
    @Getter
    public static class CreateMemberProfileImageDto {
        private String memberProfileImageURL;
    }

    @Builder
    @Getter
    public static class CreateIntroductionDto {
        @NotBlank(message = "자기소개를 비워둘 수 없습니다.")
        @Size(max = 500, message = "자기소개는 500자를 초과할 수 없습니다.")
        private String introduction;
    }

    @Builder
    @Getter
    public static class UpdateMemberProfileDto {
        private String memberProfileImageURL;
        private String nickname;
        @Size(max = 500, message = "자기소개는 500자를 초과할 수 없습니다.")
        private String introduction;
    }
}
