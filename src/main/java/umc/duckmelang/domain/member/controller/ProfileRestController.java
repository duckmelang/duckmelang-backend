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
import umc.duckmelang.domain.member.validation.annotation.ExistsMember;
import umc.duckmelang.domain.memberprofileimage.converter.MemberProfileImageConverter;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.dto.MemberProfileImageResponseDto;
import umc.duckmelang.domain.memberprofileimage.service.MemberProfileImageQueryService;
import umc.duckmelang.domain.post.converter.PostConverter;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.dto.PostResponseDto;
import umc.duckmelang.domain.post.service.PostQueryService;
import umc.duckmelang.global.apipayload.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/profile")
public class ProfileRestController {
    private final ProfileFacadeService profileFacadeService;
    private final MemberProfileImageQueryService memberProfileImageQueryService;
    private final PostQueryService postQueryService;

    @GetMapping(path = "/{memberId}")
    @Operation(summary = "다른 멤버 프로필 조회",description = "path variable로 프로필을 조회하고자하는 상대 member의 id를 받습니다.")
    ApiResponse<MemberResponseDto.OtherProfileDto> getOtherProfile(@PathVariable @ExistsMember Long memberId, int page) {
        return ApiResponse.onSuccess(profileFacadeService.getOtherProfileByMemberId(memberId, page));
    }

    @GetMapping(path = "/{memberId}/images")
    @Operation(summary = "다른 멤버 프로필 사진 조회",description = "path variable로 프로필 사진들을 조회하고자하는 상대 member의 id를 받습니다.")
    ApiResponse<MemberProfileImageResponseDto.GetAllProfileImageResultDto> getProfileImages(@PathVariable @ExistsMember Long memberId, int page) {
        Page<MemberProfileImage> memberProfileImagePage = memberProfileImageQueryService.getAllMemberProfileImageByMemberId(memberId, page);
        return ApiResponse.onSuccess(MemberProfileImageConverter.toGetAllProfileImageResultDto(memberProfileImagePage));
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
}
