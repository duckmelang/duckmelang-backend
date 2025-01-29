package umc.duckmelang.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class MemberRequestDto {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SelectIdolsDto{
        @NotEmpty(message = "최소 하나의 아이돌을 선택해야 합니다.")
        @Size(min = 1, message = "최소 하나의 아이돌을 선택해야 합니다.")
        private List<Long> idolCategoryIds;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SelectEventsDto{
        private List<Long> eventCategoryIds;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateLandminesDto {
        private List<String> landmineContents;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateMemberProfileImageDto {
        private String memberProfileImageURL;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateIntroductionDto {
        @NotBlank(message = "자기소개를 비워둘 수 없습니다.")
        @Size(max = 500, message = "자기소개는 500자를 초과할 수 없습니다.")
        private String introduction;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateMemberProfileDto {
        private String memberProfileImageURL;
        private String nickname;
        @Size(max = 500, message = "자기소개는 500자를 초과할 수 없습니다.")
        private String introduction;
    }
}
