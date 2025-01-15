package umc.duckmelang.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class MemberRequestDto {

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
    public static class SelectLandminesDto {
        private List<String> landmineContents;
    }
}
