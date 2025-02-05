package umc.duckmelang.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.duckmelang.domain.eventcategory.domain.EventCategory;
import umc.duckmelang.domain.member.domain.enums.Gender;

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
        private String nickname;
        private Gender gender;
        private Integer age;
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

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReviewInformationDto{
        private Long applicationId;
        private String name;
        private String title;
        private String eventCategory;
        private LocalDate date;
        private String postImageUrl;
        private String latestPublicMemberProfileImage;

    }
}
