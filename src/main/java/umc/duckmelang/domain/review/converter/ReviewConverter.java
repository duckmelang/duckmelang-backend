package umc.duckmelang.domain.review.converter;

import org.springframework.stereotype.Component;
import umc.duckmelang.domain.application.domain.Application;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.post.converter.PostConverter;
import umc.duckmelang.domain.post.dto.PostResponseDto;
import umc.duckmelang.domain.review.domain.Review;
import umc.duckmelang.domain.review.dto.ReviewRequestDto;
import umc.duckmelang.domain.review.dto.ReviewResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReviewConverter {

    public static Review toReview(ReviewRequestDto.ReviewJoinDto request, Member sender, Member receiver, Application application) {
        return Review.builder()
                .score(request.getScore())
                .content(request.getContent())
                .sender(sender)
                .receiver(receiver)
                .application(application)
                .build();

    }

    public static ReviewResponseDto.ReviewJoinResultDto reviewJoinResultDto(Review review) {
        return ReviewResponseDto.ReviewJoinResultDto.builder()
                .reviewId(review.getId())
                .score(review.getScore())
                .content(review.getContent())
                .receiverId(review.getReceiver().getId())
                .build();
    }

    public static ReviewResponseDto.ReviewDto reviewDto(Review review) {
        return ReviewResponseDto.ReviewDto.builder()
                .reviewId(review.getId())
                .name(review.getSender().getNickname())
                .gender(review.getSender().getGender())
                .birth(review.getSender().getBirth())
                .content(review.getContent())
                .score(review.getScore())
                .build();
    }

    public static ReviewResponseDto.ReviewListDto reviewListDto(List<Review> reviewList) {
        List<ReviewResponseDto.ReviewDto> reviewDtoList = reviewList.stream()
                .map(ReviewConverter::reviewDto).toList();

        //리뷰 score 평균값 계산
        double average = reviewList.stream()
                .mapToInt(Review::getScore)
                .average()
                .orElse(0);
        double roundedAverage = Math.round(average *10) /10.0;

        return ReviewResponseDto.ReviewListDto.builder()
                .average(roundedAverage)
                .reviewList(reviewDtoList)
                .build();

    }


}