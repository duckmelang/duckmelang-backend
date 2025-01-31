package umc.duckmelang.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.duckmelang.domain.member.facade.ProfileFacadeService;
import umc.duckmelang.domain.member.dto.MemberResponseDto;
import umc.duckmelang.domain.member.validation.annotation.ExistMember;
import umc.duckmelang.domain.member.validation.annotation.ExistsMember;
import umc.duckmelang.domain.memberprofileimage.converter.MemberProfileImageConverter;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.dto.MemberProfileImageResponseDto;
import umc.duckmelang.domain.memberprofileimage.service.MemberProfileImageQueryService;
import umc.duckmelang.domain.post.converter.PostConverter;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.dto.PostResponseDto;
import umc.duckmelang.domain.post.service.PostQueryService;
import umc.duckmelang.domain.review.converter.ReviewConverter;
import umc.duckmelang.domain.review.domain.Review;
import umc.duckmelang.domain.review.dto.ReviewResponseDto;
import umc.duckmelang.domain.review.service.ReviewQueryService;
import umc.duckmelang.global.annotations.CommonApiResponses;
import umc.duckmelang.global.apipayload.ApiResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/profile")
public class ProfileRestController {
    private final ProfileFacadeService profileFacadeService;
    private final MemberProfileImageQueryService memberProfileImageQueryService;
    private final PostQueryService postQueryService;
    private final ReviewQueryService reviewQueryService;

    @GetMapping(path = "/{memberId}")
    @Operation(summary = "다른 멤버 프로필 조회",description = "path variable로 프로필을 조회하고자하는 상대 member의 id를 받습니다.")
    ApiResponse<MemberResponseDto.OtherProfileDto> getOtherProfile(@PathVariable @ExistsMember Long memberId, int page) {
        return ApiResponse.onSuccess(profileFacadeService.getOtherProfileByMemberId(memberId, page));
    }

    @GetMapping(path = "/{memberId}/images")
    @Operation(summary = "다른 멤버 프로필 사진 조회",description = "path variable로 프로필 사진들을 조회하고자하는 상대 member의 id를 받습니다.")
    ApiResponse<MemberProfileImageResponseDto.MemberProfileImageListDto> getProfileImages(@PathVariable @ExistsMember Long memberId, int page) {
        Page<MemberProfileImage> memberProfileImagePage = memberProfileImageQueryService.getPublicMemberProfileImageByMemberId(memberId, page);
        return ApiResponse.onSuccess(MemberProfileImageConverter.toMemberProfileImageListDto(memberProfileImagePage));
    }

    @GetMapping(path = "/{memberId}/posts")
    @Operation(summary = "다른 멤버가 업로드한 게시글들 조회",description = "path variable로 게시글 조회하고자하는 상대 member의 id를 받습니다.")
    ApiResponse<PostResponseDto.PostPreviewListDto> getOtherPostList(@PathVariable @ExistsMember Long memberId, int page) {
        if(page<0){
            throw new IllegalArgumentException("페이지 번호는 0 이상어야합니다");
        }
        Page<Post> postList = postQueryService.getPostListByMember(memberId, page);
        return ApiResponse.onSuccess(PostConverter.postPreviewListDto(postList));
    }

    @GetMapping("/{memberId}/reviews")
    @CommonApiResponses
    @Operation(summary = "다른 사람의 동행 후기 조회 API", description = "다른 사람의 프로필에서 동행 후기 볼 때 이용하는 API 입니다. 성별은 true일때 남자, false일때 여자입니다.")
    public ApiResponse<ReviewResponseDto.ReviewListDto> getOtherReviewList(@ExistMember @PathVariable(name="memberId") Long memberId){
        List<Review> reviewList = reviewQueryService.getReviewList(memberId);
        double averageScore = reviewQueryService.calculateAverageScore(reviewList);
        return ApiResponse.onSuccess(ReviewConverter.reviewListDto(reviewList, averageScore));
    }
}
