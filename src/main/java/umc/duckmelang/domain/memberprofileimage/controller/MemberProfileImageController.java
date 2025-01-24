package umc.duckmelang.domain.memberprofileimage.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.duckmelang.domain.member.dto.MemberRequestDto;
import umc.duckmelang.domain.memberprofileimage.converter.MemberProfileImageConverter;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.dto.MemberProfileImageRequestDto;
import umc.duckmelang.domain.memberprofileimage.dto.MemberProfileImageResponseDto;
import umc.duckmelang.domain.memberprofileimage.service.MemberProfileImageCommandService;
import umc.duckmelang.domain.memberprofileimage.service.MemberProfileImageQueryService;
import umc.duckmelang.global.apipayload.ApiResponse;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.handler.MemberProfileImageHandler;

import java.util.List;

@RestController
@RequestMapping("/profile/image")
@RequiredArgsConstructor
public class MemberProfileImageController {

    private final MemberProfileImageQueryService memberProfileImageQueryService;
    private final MemberProfileImageCommandService memberProfileImageCommandService;

    @Operation(summary = "내 프로필 사진 전체 조회 API", description = "특정 회원 본인의 프로필 사진을 모두 조회하는 API입니다. 비공개된 사진과 공개된 사진 모두 확인 가능합니다.")
    @GetMapping("/")
    public ApiResponse<MemberProfileImageResponseDto.GetAllProfileImageResultDto> getAllProfileImages (@RequestParam Long memberId ) {

        List<MemberProfileImage> updatedMemberProfileImageList = memberProfileImageQueryService.getAllMemberProfileImageByMemberId(memberId);

        // 리스트가 비어 있는 경우 예외 처리
        if (updatedMemberProfileImageList.isEmpty()) {
            throw new MemberProfileImageHandler(ErrorStatus.MEMBERPROFILEIMAGE_NOT_FOUND);
        }

        return ApiResponse.onSuccess(MemberProfileImageConverter.toGetAllProfileImageResultDto(updatedMemberProfileImageList));
    }

    @Operation(summary = "내 프로필 사진 삭제 API", description = "특정 회원 본인의 프로필 사진 중 하나를 삭제하는 API입니다.")
    @DeleteMapping("/")
    public ApiResponse<MemberProfileImageResponseDto.DeleteProfileImageResultDto> deleteProfileImage (
            @RequestParam Long memberId,  // 임시로 사용. 나중에 JWT에서 추출할 예정
            @RequestBody @Valid MemberProfileImageRequestDto.DeleteMemberProfileImageDto request) {

        memberProfileImageCommandService.deleteProfileImage(memberId, request);

        return ApiResponse.onSuccess(MemberProfileImageConverter.toDeleteProfileImageResultDto());
    }



}
