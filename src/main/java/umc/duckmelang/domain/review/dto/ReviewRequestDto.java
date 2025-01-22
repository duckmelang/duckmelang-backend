package umc.duckmelang.domain.review.dto;

import lombok.Getter;
import lombok.Setter;

public class ReviewRequestDto {
    @Getter
    @Setter
    public static class ReviewJoinDto{
        private Short score;
        private String content;
        private Long receiverId;
    }
}
