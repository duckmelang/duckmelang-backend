package umc.duckmelang.domain.review.converter;

import org.springframework.stereotype.Component;
import umc.duckmelang.domain.eventcategory.domain.EventCategory;
import umc.duckmelang.domain.idolcategory.domain.IdolCategory;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.dto.PostRequestDto;
import umc.duckmelang.domain.post.dto.PostResponseDto;
import umc.duckmelang.domain.review.domain.Review;
import umc.duckmelang.domain.review.dto.ReviewRequestDto;
import umc.duckmelang.domain.review.dto.ReviewResponseDto;

import java.util.List;

@Component
public class ReviewConverter {

    public static Review toReview(ReviewRequestDto.ReviewJoinDto request, Member sender, Member receiver) {
        return Review.builder()
                .score(request.getScore())
                .content(request.getContent())
                .sender(sender)
                .receiver(receiver)
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
}

