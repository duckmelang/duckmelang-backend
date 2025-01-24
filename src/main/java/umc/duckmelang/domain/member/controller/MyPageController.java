package umc.duckmelang.domain.member.controller;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.duckmelang.domain.application.service.ApplicationQueryService;
import umc.duckmelang.domain.member.converter.MemberConverter;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.dto.MemberRequestDto;
import umc.duckmelang.domain.member.dto.MemberResponseDto;
import umc.duckmelang.domain.member.service.MemberCommandService;
import umc.duckmelang.domain.member.service.MemberQueryService;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.dto.MemberProfileImageRequestDto;
import umc.duckmelang.domain.memberprofileimage.service.MemberProfileImageCommandService;
import umc.duckmelang.domain.memberprofileimage.service.MemberProfileImageQueryService;
import umc.duckmelang.domain.post.service.PostQueryService;
import umc.duckmelang.global.apipayload.ApiResponse;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.handler.MemberHandler;
import umc.duckmelang.global.apipayload.exception.handler.MemberProfileImageHandler;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;
    private final MemberProfileImageQueryService memberProfileImageQueryService;
    private final MemberProfileImageCommandService memberProfileImageCommandService;
    private final PostQueryService postQueryService;
    private final ApplicationQueryService applicationQueryService;

    @Operation(summary = "마이페이지 조회 API", description = "마이페이지 첫 화면에 노출되는 회원 정보를 조회해오는 API입니다. member의 id, nickname, gender, age, 대표 프로필 사진을 불러옵니다.")
    @GetMapping("/mypage")
    public ApiResponse<MemberResponseDto.GetMypageMemberPreviewResultDto> getMypageMemberPreview (@RequestParam Long memberId) {

        Member retrievedMember = memberQueryService.getMemberById(memberId)
                .orElseThrow(()-> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        MemberProfileImage latestPublicMemberProfileImage = memberProfileImageQueryService.getLatestPublicMemberProfileImage(memberId)
                .orElseThrow(()-> new MemberProfileImageHandler(ErrorStatus.MEMBERPROFILEIMAGE_NOT_FOUND));
        return ApiResponse.onSuccess(MemberConverter.toGetMemberPreviewResponseDto(retrievedMember, latestPublicMemberProfileImage));
    }

    @Operation(summary = "내 프로필 조회 API", description = "마이페이지를 통해 접근할 수 있는 내 프로필을 조회해오는 API입니다. member의 id, nickname, gender, age, introduction, 대표 프로필 사진, 특정 member가 작성한 게시글 수, 특정 member의 매칭 횟수를 불러옵니다. ")
    @GetMapping("/profile")
    public ApiResponse<MemberResponseDto.GetMypageMemberProfileResultDto> getMypageMemberProfile (@RequestParam Long memberId) {

        Member retrievedMember = memberQueryService.getMemberById(memberId)
                .orElseThrow(()-> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        MemberProfileImage latestPublicMemberProfileImage = memberProfileImageQueryService.getLatestPublicMemberProfileImage(memberId)
                .orElseThrow(()-> new MemberProfileImageHandler(ErrorStatus.MEMBERPROFILEIMAGE_NOT_FOUND));

        long postCount = postQueryService.getPostCountByMemberId(memberId);

        long succeedApplicationCount = applicationQueryService.getSucceedApplicationCountByMemberId(memberId);

        return ApiResponse.onSuccess(MemberConverter.toGetMemberProfileResponseDto(retrievedMember, latestPublicMemberProfileImage, postCount, succeedApplicationCount));
    }


    @Operation(summary = "내 프로필 수정 API", description = "마이페이지를 통해 접근할 수 있는 내 프로필을 수정하는 API입니다. member의 nickname introduction을 수정하고 프로필 사진을 생성할 수 있습니다. ")
    @PatchMapping("/profile/edit")
    public ApiResponse<MemberResponseDto.GetMypageMemberProfileEditResultDto> updateMypageMemberProfile
            (@RequestParam Long memberId,
             @RequestBody MemberRequestDto.UpdateMemberProfileDto request) {

        Member retrievedMember = memberQueryService.getMemberById(memberId)
                .orElseThrow(()-> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Member updatedMember = memberCommandService.updateMemberProfile(memberId, request);

        MemberProfileImage updatedMemberProfileImage = memberProfileImageCommandService.createMemberProfile(memberId, request.getMemberProfileImageURL());

        MemberProfileImage latestPublicMemberProfileImage = memberProfileImageQueryService.getLatestPublicMemberProfileImage(memberId)
                .orElseThrow(()-> new MemberProfileImageHandler(ErrorStatus.MEMBERPROFILEIMAGE_NOT_FOUND));


        return ApiResponse.onSuccess(MemberConverter.toUpdateMemberProfileDto(updatedMember,latestPublicMemberProfileImage));
    }


}
