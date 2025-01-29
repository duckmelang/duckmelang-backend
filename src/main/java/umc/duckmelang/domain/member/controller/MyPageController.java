package umc.duckmelang.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import umc.duckmelang.domain.application.service.ApplicationQueryService;
import umc.duckmelang.domain.member.converter.MemberConverter;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.dto.MemberRequestDto;
import umc.duckmelang.domain.member.dto.MemberResponseDto;
import umc.duckmelang.domain.member.facade.ProfileFacadeService;
import umc.duckmelang.domain.member.service.MemberCommandService;
import umc.duckmelang.domain.member.service.MemberQueryService;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.dto.MemberProfileImageRequestDto;
import umc.duckmelang.domain.memberprofileimage.service.MemberProfileImageCommandService;
import umc.duckmelang.domain.memberprofileimage.service.MemberProfileImageQueryService;
import umc.duckmelang.domain.post.converter.PostConverter;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.dto.PostResponseDto;
import umc.duckmelang.domain.post.converter.PostConverter;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.dto.PostResponseDto;
import umc.duckmelang.domain.post.service.PostQueryService;

import umc.duckmelang.global.apipayload.ApiResponse;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;
    private final MemberProfileImageQueryService memberProfileImageQueryService;
    private final MemberProfileImageCommandService memberProfileImageCommandService;
    private final ProfileFacadeService profileFacadeService;
    private final PostQueryService postQueryService;

    @GetMapping(path = "/posts")
    @Operation(summary = "내가 업로드한 게시글들 조회",description = "memberId는 추후 jwt로 바꿉니다")
    ApiResponse<PostResponseDto.PostPreviewListDto> getMyPostList(@RequestParam Long memberId, int page) {
        if(page<0){
            throw new IllegalArgumentException("페이지 번호는 0 이상어야합니다");
        }
        Page<Post> postList = postQueryService.getPostListByMember(memberId, page);
        return ApiResponse.onSuccess(PostConverter.postPreviewListDto(postList));
    }

    @Operation(summary = "마이페이지 조회 API", description = "마이페이지 첫 화면에 노출되는 회원 정보를 조회해오는 API입니다. member의 id, nickname, gender, age, 대표 프로필 사진을 불러옵니다.")
    @GetMapping("/mypage")
    public ApiResponse<MemberResponseDto.GetMypageMemberPreviewResultDto> getMypageMemberPreview (@RequestParam Long memberId) {

        return ApiResponse.onSuccess(profileFacadeService.getMypageMemberPreview(memberId));
    }

    @Operation(summary = "내 프로필 조회 API", description = "마이페이지를 통해 접근할 수 있는 내 프로필을 조회해오는 API입니다. member의 id, nickname, gender, age, introduction, 대표 프로필 사진, 특정 member가 작성한 게시글 수, 특정 member의 매칭 횟수를 불러옵니다. ")
    @GetMapping("/profile")
    public ApiResponse<MemberResponseDto.GetMypageMemberProfileResultDto> getMypageMemberProfile (@RequestParam Long memberId) {

        return ApiResponse.onSuccess(profileFacadeService.getMyProfileByMemberId(memberId));
    }


    @Operation(summary = "내 프로필 수정 API", description = "마이페이지를 통해 접근할 수 있는 내 프로필을 수정하는 API입니다. member의 nickname introduction을 수정하고 프로필 사진을 생성할 수 있습니다. ")
    @PatchMapping("/profile/edit")
    public ApiResponse<MemberResponseDto.GetMypageMemberProfileEditResultDto> updateMypageMemberProfile
            (@RequestParam Long memberId,
             @RequestBody MemberRequestDto.UpdateMemberProfileDto request) {

        return ApiResponse.onSuccess(profileFacadeService.updateMypageMemberProfile(memberId, request));
    }


}
