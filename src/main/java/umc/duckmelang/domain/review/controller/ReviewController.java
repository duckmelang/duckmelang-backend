package umc.duckmelang.domain.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import umc.duckmelang.domain.member.validation.annotation.ExistMember;
import umc.duckmelang.domain.post.converter.PostConverter;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.validation.annotation.ValidPageNumber;
import umc.duckmelang.domain.review.converter.ReviewConverter;
import umc.duckmelang.domain.review.domain.Review;
import umc.duckmelang.domain.review.dto.ReviewRequestDto;
import umc.duckmelang.domain.review.dto.ReviewResponseDto;
import umc.duckmelang.domain.review.repository.ReviewRepository;
import umc.duckmelang.domain.review.service.ReviewCommandService;
import umc.duckmelang.domain.review.service.ReviewQueryService;
import umc.duckmelang.global.annotations.CommonApiResponses;
import umc.duckmelang.global.apipayload.ApiResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class ReviewController {
    private final ReviewCommandService reviewCommandService;
    private final ReviewQueryService reviewQueryService;

    @PostMapping("/reviews")
    @CommonApiResponses
    @Operation(summary = "후기글 작성 API", description = "후기글 작성 API입니다. Sender(memberId), Receiver(receiverId)를 써주세요. applicationId는 후기글 작성 페이지 내 관련 정보 조회 API에서 얻어오면 됩니다")
    public ApiResponse<ReviewResponseDto.ReviewJoinResultDto> joinReview (@RequestParam(name="memberId") Long memberId, @RequestBody @Valid ReviewRequestDto.ReviewJoinDto request){
        Review review = reviewCommandService.joinReview(request, memberId);
        return ApiResponse.onSuccess(ReviewConverter.reviewJoinResultDto(review));
    }

    @GetMapping("/profile/reviews")
    @CommonApiResponses
    @Operation(summary = "나와의 동행 후기 조회 API", description = "내 프로필에서 나와의 동행 후기 볼 때 이용하는 API 입니다. memberId는 추후 JWT 변경 예정")
    public ApiResponse<ReviewResponseDto.ReviewListDto> getMyReviewList(@RequestParam(name="memberId") Long memberId){
        List<Review> reviewList = reviewQueryService.getReviewList(memberId);
        return ApiResponse.onSuccess(ReviewConverter.reviewListDto(reviewList));
    }

    @GetMapping("/profile/{memberId}/reviews/")
    @CommonApiResponses
    @Operation(summary = "다른 사람의 동행 후기 조회 API", description = "다른 사람의 프로필에서 동행 후기 볼 때 이용하는 API 입니다.")
    public ApiResponse<ReviewResponseDto.ReviewListDto> getOtherReviewList(@ExistMember @PathVariable(name="memberId") Long memberId){
        List<Review> reviewList = reviewQueryService.getReviewList(memberId);
        return ApiResponse.onSuccess(ReviewConverter.reviewListDto(reviewList));
    }

}
