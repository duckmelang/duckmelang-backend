package umc.duckmelang.domain.member.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import umc.duckmelang.domain.member.converter.MemberConverter;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.dto.MemberResponseDto;
import umc.duckmelang.domain.member.service.MemberQueryService;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.service.MemberProfileImageQueryService;
import umc.duckmelang.global.apipayload.ApiResponse;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.handler.MemberHandler;
import umc.duckmelang.global.apipayload.exception.handler.MemberProfileImageHandler;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final MemberQueryService memberQueryService;
    private final MemberProfileImageQueryService memberProfileImageQueryService;


    // 마이페이지 화면 내 기능 API
    @Operation(summary = "마이페이지 조회 API", description = "마이페이지 첫 화면에 노출되는 회원 정보를 조회해오는 API입니다. member의 id, nickname, gender, age, 대표프로필사진을 불러옵니다.")
    @GetMapping("/mypage")
    public ApiResponse<MemberResponseDto.GetMypageMemberPreviewResultDto> getMypageMemberPreview (@PathVariable(name = "memberId") Long memberId) {

        Member retrievedMember = memberQueryService.getMemberById(memberId)
                .orElseThrow(()-> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        MemberProfileImage latestPublicMemberProfileImage = memberProfileImageQueryService.getLatestPublicMemberProfileImage(memberId)
                .orElseThrow(()-> new MemberProfileImageHandler(ErrorStatus.MEMBERPROFILEIMAGE_NOT_FOUND));
        return ApiResponse.onSuccess(MemberConverter.toGetMemberPreviewResponseDto(retrievedMember, latestPublicMemberProfileImage));
    }

}
