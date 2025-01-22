package umc.duckmelang.domain.review.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class ReviewRequestDto {
    @Getter
    @Setter
    public static class ReviewJoinDto{
        @NotNull
        private Short score;
        @NotNull
        private String content;
        @NotNull
        private Long receiverId;
    }
}
