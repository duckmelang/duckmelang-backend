package umc.duckmelang.domain.review.converter;

import org.springframework.stereotype.Component;
import umc.duckmelang.domain.application.domain.Application;
import umc.duckmelang.domain.eventcategory.domain.EventCategory;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.post.converter.PostConverter;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.dto.PostResponseDto;
import umc.duckmelang.domain.review.domain.Review;
import umc.duckmelang.domain.review.dto.ReviewRequestDto;
import umc.duckmelang.domain.review.dto.ReviewResponseDto;

import java.time.LocalDate;
import java.util.Comparator;
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
                .nickname(review.getSender().getNickname())
                .gender(review.getSender().stringGender())
                .age(review.getSender().calculateAge())
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

    public static ReviewResponseDto.ReviewInformationDto reviewInformationDto(Application application, Member member) {
        MemberProfileImage latestImage = member.getMemberProfileImageList().stream()
                .filter(MemberProfileImage::isPublic)
                .max(Comparator.comparing(MemberProfileImage::getUpdatedAt)) // 최신 updatedAt 기준
                .orElse(null); // 없으면 null

        return ReviewResponseDto.ReviewInformationDto.builder()
                .applicationId(application.getId())
                .name(application.getPost().getMember().getNickname())
                .title(application.getPost().getTitle())
                .eventCategory(application.getPost().getEventCategory().getName())
                .date(application.getPost().getEventDate())
                .postImageUrl(application.getPost().getPostImageList().isEmpty() ? null : application.getPost().getPostImageList().get(0).getPostImageUrl())
                .latestPublicMemberProfileImage(latestImage != null ? latestImage.getMemberImage() : null)
                .build();
    }
}