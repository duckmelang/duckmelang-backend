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
}
