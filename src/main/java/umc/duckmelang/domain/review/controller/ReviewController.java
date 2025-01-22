package umc.duckmelang.domain.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import umc.duckmelang.domain.review.converter.ReviewConverter;
import umc.duckmelang.domain.review.domain.Review;
import umc.duckmelang.domain.review.dto.ReviewRequestDto;
import umc.duckmelang.domain.review.dto.ReviewResponseDto;
import umc.duckmelang.domain.review.repository.ReviewRepository;
import umc.duckmelang.domain.review.service.ReviewCommandService;
import umc.duckmelang.global.annotations.CommonApiResponses;
import umc.duckmelang.global.apipayload.ApiResponse;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Validated
public class ReviewController {
    private final ReviewCommandService reviewCommandService;

    @PostMapping("/{memberId}")
    @CommonApiResponses
    @Operation(summary = "후기글 작성 API", description = "후기글 작성 API입니다. Sender(memberId), Receiver(receiverId)를 써주세요")
    public ApiResponse<ReviewResponseDto.ReviewJoinResultDto> joinReview (@PathVariable(name="memberId") Long memberId, @RequestBody @Valid ReviewRequestDto.ReviewJoinDto request){
        Review review = reviewCommandService.joinReview(request, memberId);
        return ApiResponse.onSuccess(ReviewConverter.reviewJoinResultDto(review));
    }
}
