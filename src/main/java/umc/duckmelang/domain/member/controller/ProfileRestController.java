package umc.duckmelang.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.duckmelang.domain.member.Facade.ProfileFacadeService;
import umc.duckmelang.domain.member.dto.MemberResponseDto;
import umc.duckmelang.domain.memberprofileimage.converter.MemberProfileImageConverter;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.dto.MemberProfileImageResponseDto;
import umc.duckmelang.domain.memberprofileimage.service.MemberProfileImageQueryService;
import umc.duckmelang.global.apipayload.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/profile")
public class ProfileRestController {
    private final ProfileFacadeService profileFacadeService;
    private final MemberProfileImageQueryService memberProfileImageQueryService;

    @GetMapping(path = "/{memberId}")
    @Operation(summary = "프로필 조회",description = "path variable로 프로필을 조회하고자하는 상대 member의 id를 받습니다.")
    ApiResponse<MemberResponseDto.OtherProfileDto> getOtherProfile(@PathVariable Long memberId, int page) {
        return ApiResponse.onSuccess(profileFacadeService.getOtherProfileByMemberId(memberId, page));
    }

    @GetMapping(path = "/{memberId}/images")
    @Operation(summary = "프로필 사진 조회",description = "path variable로 프로필 사진들을 조회하고자하는 상대 member의 id를 받습니다.")
    ApiResponse<MemberProfileImageResponseDto.MemberProfileImageListDto> getProfileImages(@PathVariable Long memberId, int page) {
        Page<MemberProfileImage> memberProfileImagePage = memberProfileImageQueryService.getProfileImagesByMemberId(memberId, page);
        return ApiResponse.onSuccess(MemberProfileImageConverter.toMemberProfileImageListDto(memberProfileImagePage));
    }
}
