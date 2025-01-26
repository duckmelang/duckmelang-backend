package umc.duckmelang.domain.review.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ReviewRequestDto {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class ReviewJoinDto{
        @NotNull
        private Short score;
        @NotNull
        private String content;
        @NotNull
        private Long receiverId;
        @NotNull
        private Long applicationId;
    }
}
