package umc.duckmelang.domain.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import umc.duckmelang.domain.application.domain.Application;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.repository.MemberRepository;
import umc.duckmelang.domain.member.validation.annotation.ExistMember;
import umc.duckmelang.domain.review.converter.ReviewConverter;
import umc.duckmelang.domain.review.domain.Review;
import umc.duckmelang.domain.review.dto.ReviewRequestDto;
import umc.duckmelang.domain.review.dto.ReviewResponseDto;
import umc.duckmelang.domain.review.service.ReviewCommandService;
import umc.duckmelang.domain.review.service.ReviewQueryService;
import umc.duckmelang.global.annotations.CommonApiResponses;
import umc.duckmelang.global.apipayload.ApiResponse;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.ApplicationException;
import umc.duckmelang.global.apipayload.exception.MemberException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
@Validated
public class ReviewRestController {
    private final ReviewCommandService reviewCommandService;
    private final ReviewQueryService reviewQueryService;
    private final MemberRepository memberRepository;

    @PostMapping("")
    @CommonApiResponses
    @Operation(summary = "후기글 작성 API", description = "후기글 작성 API입니다. Sender(memberId), Receiver(receiverId)를 써주세요. applicationId는 후기글 작성 페이지 내 관련 정보 조회 API에서 얻어오면 됩니다")
    public ApiResponse<ReviewResponseDto.ReviewJoinResultDto> joinReview (@RequestParam(name="memberId") Long memberId, @RequestBody @Valid ReviewRequestDto.ReviewJoinDto request){
        Review review = reviewCommandService.joinReview(request, memberId);
        return ApiResponse.onSuccess(ReviewConverter.reviewJoinResultDto(review));
    }

    @GetMapping("/information")
    @CommonApiResponses
    @Operation(summary = "후기글 작성 페이지 내 관련 정보 조회 API", description = "후기글 작성 페이지에서 applicationId 외에 유저네임, 게시글 제목, 행사 날짜 등 정보를 보여주는 API 입니다. 최신 순으로 정렬되어 list로 내보냅니다. memberId를 requestParam으로 넣어주세요. myId는 추후 JWT 추출 예정")
    public ApiResponse<List<ReviewResponseDto.ReviewInformationDto>> getReviewInformation(@ExistMember @RequestParam(name="memberId") Long memberId, @RequestParam(name="myId") Long myId){
        List<Application> applications = reviewQueryService.getReviewInformation(myId, memberId);

        if (applications.isEmpty()) {
            throw new ApplicationException(ErrorStatus.APPLICATION_NOT_FOUND);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        List<ReviewResponseDto.ReviewInformationDto> reviewInformationDtos = applications.stream()
                .map(application -> ReviewConverter.reviewInformationDto(application, member ))
                .collect(Collectors.toList());

        return ApiResponse.onSuccess(reviewInformationDtos);
    }

}
