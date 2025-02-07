package umc.duckmelang.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.duckmelang.domain.member.converter.MemberConverter;
import umc.duckmelang.domain.member.converter.MemberProfileConverter;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.dto.MemberResponseDto;
import umc.duckmelang.domain.member.dto.MyPageRequestDto;
import umc.duckmelang.domain.member.dto.MyPageResponseDto;
import umc.duckmelang.domain.member.service.mypage.MyPageCommandService;
import umc.duckmelang.domain.member.service.mypage.MyPageQueryService;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.service.MemberProfileImageCommandService;
import umc.duckmelang.domain.post.service.PostCommandService;
import umc.duckmelang.global.apipayload.ApiResponse;
import umc.duckmelang.global.validation.annotation.ExistPost;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
@Validated
public class MyPageManagementRestController {
    private final MyPageCommandService myPageCommandService;
    private final MemberProfileImageCommandService memberProfileImageCommandService;
    private final MyPageQueryService myPageQueryService;
    private final PostCommandService postCommandService;

    @Operation(summary = "내 프로필 수정 API - 기존 프로필 정보 조회", description = "피그마 상에서는 기존 프로필 사진만 조회하게 되어있는데 혹시 닉네임도 필요하실까 해서 일단 넣어놓았습니다.")
    @GetMapping("/profile/edit")
    public ApiResponse<MyPageResponseDto.MyPagProfileEditBeforeDto> getMyPageMemberProfileImage(@RequestParam("memberId") Long memberId){
        return ApiResponse.onSuccess(myPageQueryService.getMemberProfileBeforeEdit(memberId));
    }

    @Operation(summary = "내 프로필 수정 API - 닉네임, 자기소개 수정", description = "내 프로필을 수정하는 API입니다. 사용자의 닉네임과 자기소개를 수정합니다.")
    @PatchMapping("/profile/edit")
    public ApiResponse<MyPageResponseDto.MyPageProfileEditAfterDto> updateMyPageMemberProfile(@RequestParam("memberId") Long memberId, @RequestBody MyPageRequestDto.UpdateMemberProfileDto request) {
        Member updatedMember = myPageCommandService.updateMemberProfile(memberId, request);
        return ApiResponse.onSuccess(MemberProfileConverter.toMemberProfileEditAfterDto(updatedMember));
    }

    @Operation(summary = "내 프로필 수정 API - 프로필 이미지 업로드", description = "내 프로필을 수정하는 API입니다. 사용자의 프로필 이미지를 추가합니다.")
    @PostMapping(value = "/profile/image/edit", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<MemberResponseDto.CreateMemberProfileImageResultDto> updateProfileImage(@RequestParam("memberId") Long memberId, @RequestPart MultipartFile profileImage){
        MemberProfileImage updatedMemberProfileImage = memberProfileImageCommandService.createProfileImage(memberId, profileImage);
        return ApiResponse.onSuccess(MemberConverter.toCreateMemberProfileImageResponseDto(updatedMemberProfileImage));
    }

    @Operation(summary = "피드 관리 - 피드 목록 삭제", description = "내 피드 목록을 삭제하는 API입니다.")
    @DeleteMapping("/posts/{postId}")
    public ApiResponse<String> deleteMyPost(@ExistPost @PathVariable("postId") Long postId){
        postCommandService.deleteMyPost(postId);
        return ApiResponse.onSuccess("피드를 성공적으로 삭제했습니다.");
    }
}
