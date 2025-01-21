package umc.duckmelang.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class MemberResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SelectIdolsResultDto{
        private Long memberId;
        private List<Long> idolCategoryIds;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SelectEventsResultDto {
        private Long memberId;
        private List<Long> eventCategoryIds;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateLandmineResultDto {
        private Long memberId;
        private List<String> landmineContents;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateMemberProfileImageResultDto {
        private Long memberId;
        private String memberProfileImageURL;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateIntroductionResultDto {
        private Long memberId;
        private String introduction;
    }
}
