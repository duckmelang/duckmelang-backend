package umc.duckmelang.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class ReviewResponseDto {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReviewJoinResultDto{
        private Long reviewId;
        private Short score;
        private String content;
        private Long receiverId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReviewDto{
        private Long reviewId;
        private String name;
        private Boolean gender;
        private LocalDate birth;
        private String content;
        private Short score;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReviewListDto{
        private double average;
        List<ReviewDto> reviewList;


    }
}
