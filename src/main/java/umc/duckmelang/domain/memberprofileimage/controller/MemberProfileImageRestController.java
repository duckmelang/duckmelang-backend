package umc.duckmelang.domain.memberprofileimage.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.duckmelang.domain.memberprofileimage.converter.MemberProfileImageConverter;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.dto.MemberProfileImageRequestDto;
import umc.duckmelang.domain.memberprofileimage.dto.MemberProfileImageResponseDto;
import umc.duckmelang.domain.memberprofileimage.service.MemberProfileImageCommandService;
import umc.duckmelang.domain.memberprofileimage.service.MemberProfileImageQueryService;
import umc.duckmelang.global.apipayload.ApiResponse;
import umc.duckmelang.global.validation.annotation.ValidPageNumber;

@RestController
@RequestMapping("/mypage/profile/image")
@RequiredArgsConstructor
public class MemberProfileImageRestController {
    private final MemberProfileImageQueryService memberProfileImageQueryService;
    private final MemberProfileImageCommandService memberProfileImageCommandService;

    @Operation(summary = "내 프로필 사진 전체 조회 API", description = "특정 회원 본인의 프로필 사진을 모두 조회하는 API입니다. 비공개된 사진과 공개된 사진 모두 확인 가능합니다.")
    @GetMapping("")
    public ApiResponse<MemberProfileImageResponseDto.MemberProfileImageListDto> getAllProfileImages(@RequestParam("memberId") Long memberId, @ValidPageNumber @RequestParam(name = "page",  defaultValue = "0") Integer page) {
        Page<MemberProfileImage> memberProfileImagePage = memberProfileImageQueryService.getAllMemberProfileImageByMemberId(memberId, page);
        return ApiResponse.onSuccess(MemberProfileImageConverter.toMemberProfileImageListDto(memberProfileImagePage));
    }

    @Operation(summary = "내 프로필 사진 삭제 API", description = "특정 회원 본인의 프로필 사진 중 하나를 삭제하는 API입니다.")
    @DeleteMapping("")
    public ApiResponse<MemberProfileImageResponseDto.DeleteProfileImageResultDto> deleteProfileImage (@RequestParam("memberId") Long memberId, @RequestBody @Valid MemberProfileImageRequestDto.MemberProfileImageDto request) {
        memberProfileImageCommandService.deleteProfileImage(memberId, request);
        return ApiResponse.onSuccess(MemberProfileImageConverter.toDeleteProfileImageResultDto());
    }

    @Operation(summary = "내 프로필 사진 공개 범위 전환 API", description = "특정 회원 본인의 프로필 사진 중 하나를 공개로 전환하거나 비공개로 전환하는 API입니다.")
    @PatchMapping("/status")
    public ApiResponse<MemberProfileImageResponseDto.UpdateProfileImageStatusResultDto> updateProfileImageStatus(@RequestParam("memberId") Long memberId, @RequestBody @Valid MemberProfileImageRequestDto.MemberProfileImageDto request) {
        MemberProfileImage updatedMemberProfileImage = memberProfileImageCommandService.updateProfileImageStatus(memberId, request);
        return ApiResponse.onSuccess(MemberProfileImageConverter.toUpdateProfileImageStatusResultDto(updatedMemberProfileImage));
    }
}
