package umc.duckmelang.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import umc.duckmelang.domain.member.domain.Member;

public class ReviewResponseDto {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class ReviewJoinResultDto{
        private Long reviewId;
        private Short score;
        private String content;
        private Long receiverId;
    }
}
