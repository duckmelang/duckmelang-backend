package umc.duckmelang.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import umc.duckmelang.domain.member.dto.MyPageResponseDto;
import umc.duckmelang.domain.member.facade.ProfileFacadeService;
import umc.duckmelang.domain.post.converter.PostConverter;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.dto.PostResponseDto;
import umc.duckmelang.domain.post.service.PostQueryService;
import umc.duckmelang.global.validation.annotation.ValidPageNumber;
import umc.duckmelang.domain.review.converter.ReviewConverter;
import umc.duckmelang.domain.review.domain.Review;
import umc.duckmelang.domain.review.dto.ReviewResponseDto;
import umc.duckmelang.domain.review.service.ReviewQueryService;
import umc.duckmelang.global.annotations.CommonApiResponses;
import umc.duckmelang.global.apipayload.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageRestController {
    private final ProfileFacadeService profileFacadeService;
    private final PostQueryService postQueryService;
    private final ReviewQueryService reviewQueryService;

    @Operation(summary = "마이페이지 조회 API", description = "마이페이지 첫 화면에 노출되는 회원 정보를 조회해오는 API입니다. 사용자의 닉네임, 성별, 나이, 대표 프로필 사진을 불러옵니다.")
    @GetMapping("")
    public ApiResponse<MyPageResponseDto.MypageMemberPreviewResultDto> getMyPageMemberPreview(@RequestParam Long memberId) {
        return ApiResponse.onSuccess(profileFacadeService.getMyPageMemberPreview(memberId));
    }

    @Operation(summary = "내 프로필 조회 API", description = "마이페이지를 통해 접근할 수 있는 내 프로필을 조회해오는 API입니다. 사용자의 닉네임, 성별, 나이, 자기소개, 대표 프로필 사진, 특정 사용자가 작성한 게시글 수, 특정 사용자의 매칭 횟수를 불러옵니다.")
    @GetMapping("/profile")
    public ApiResponse<MyPageResponseDto.ProfileDto> getMyPageMemberProfile(@RequestParam Long memberId) {
        return ApiResponse.onSuccess(profileFacadeService.getProfileByMemberId(memberId));
    }

    @GetMapping("/reviews")
    @CommonApiResponses
    @Operation(summary = "나와의 동행 후기 조회 API", description = "내 프로필에서 나와의 동행 후기 볼 때 이용하는 API 입니다.")
    public ApiResponse<ReviewResponseDto.ReviewListDto> getMyReviewList(@RequestParam Long memberId){
        List<Review> reviewList = reviewQueryService.getReviewList(memberId);
        double averageScore = reviewQueryService.calculateAverageScore(reviewList);
        return ApiResponse.onSuccess(ReviewConverter.reviewListDto(reviewList, averageScore));
    }

    @GetMapping("/posts")
    @Operation(summary = "내가 업로드한 게시글들 조회 API & 피드 관리 - 내 피드 목록 조회", description = "마이페이지에서 업로드한 게시글 조회와 피드 관리에서 내 피드 목록 조회에 사용되는 API입니다.")
    ApiResponse<PostResponseDto.PostPreviewListDto> getMyPostList(@RequestParam Long memberId, @ValidPageNumber @RequestParam(name = "page",  defaultValue = "0") Integer page) {
        Page<Post> postList = postQueryService.getPostListByMember(memberId, page);
        return ApiResponse.onSuccess(PostConverter.postPreviewListDto(postList));
    }
}
