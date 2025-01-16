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
    public static class SelectLandmineResultDto {
        private Long memberId;
        private List<String> landmineContents;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SelectMemberProfileImageResultDto{
        private Long memberId;
        private String memberProfileImageURL;
    }
}
