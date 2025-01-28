package umc.duckmelang.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.duckmelang.domain.landmine.domain.Landmine;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import umc.duckmelang.domain.member.converter.MemberConverter;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.dto.MemberRequestDto;
import umc.duckmelang.domain.member.dto.MemberResponseDto;
import umc.duckmelang.domain.member.service.MemberCommandService;
import umc.duckmelang.domain.memberevent.domain.MemberEvent;
import umc.duckmelang.domain.memberidol.domain.MemberIdol;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.global.apipayload.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberCommandService memberCommandService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입 API", description = "사용자 정보를 받아 회원가입을 처리하는 API입니다.")
    public ApiResponse<MemberResponseDto.SignupResultDto> signup(@RequestBody @Valid MemberRequestDto.SignupDto request){
        Member member = memberCommandService.signupMember(request);
        return ApiResponse.onSuccess(MemberConverter.toSignupResultDto(member));
    }

    @Operation(summary = "덕질하는 아이돌 선택 API", description = "회원이 덕질하는 아이돌(들)을 선택하는 API입니다. 최소 하나의 아이돌은 선택해야 합니다.")
    @PostMapping("/{memberId}/interesting-idol")
    public ApiResponse<MemberResponseDto.SelectIdolsResultDto> selectIdols(
            @PathVariable(name = "memberId") Long memberId,
            @RequestBody @Valid MemberRequestDto.SelectIdolsDto request) {

        List<MemberIdol> updatedMemberIdolList = memberCommandService.selectIdols(memberId, request);

        // 리스트가 비어 있는 경우 예외 처리
        if (updatedMemberIdolList.isEmpty()) {
            throw new IllegalArgumentException("선택된 아이돌 카테고리가 없습니다.");
        }

        return ApiResponse.onSuccess(MemberConverter.toSelectIdolResponseDto(updatedMemberIdolList));
    }

    @Operation(summary = "선호하는 행사 종류 선택 API", description = "회원이 선호하는 행사(들)를 선택하는 API입니다. 하나도 선택하지 않을 수 있습니다.(이 경우 빈 리스트를 반환합니다)")
    @PostMapping("/{memberId}/interesting-event")
    public ApiResponse<MemberResponseDto.SelectEventsResultDto> selectEvents(
            @PathVariable(name = "memberId") Long memberId,
            @RequestBody @Valid MemberRequestDto.SelectEventsDto request) {

        List<MemberEvent> updatedMemberEventList = memberCommandService.selectEvents(memberId, request);

        // 리스트가 비어 있는 경우 허용(따라서 예외 처리 생략)

        return ApiResponse.onSuccess(MemberConverter.toSelectEventResponseDto(updatedMemberEventList));
    }

    @Operation(summary = "지뢰 설정 API", description = "회원이 기피하는 키워드(들)를 선택하는 API입니다. 하나도 선택하지 않을 수 있습니다.(이 경우 빈 리스트를 반환합니다)")
    @PostMapping("/{memberId}/landmine")
    public ApiResponse<MemberResponseDto.CreateLandmineResultDto> createLandmines(
            @PathVariable(name = "memberId") Long memberId,
            @RequestBody @Valid MemberRequestDto.CreateLandminesDto request) {

        List<Landmine> updatedLandmineList = memberCommandService.createLandmines(memberId, request);

        // 리스트가 비어 있는 경우 허용(따라서 예외 처리 생략)

        return ApiResponse.onSuccess(MemberConverter.toCreateLandmineResponseDto(updatedLandmineList));
    }

    @Operation(summary = "프로필 사진 설정 API", description = "회원이 최초로 프로필 사진을 설정하는 API입니다. 프로필 사진을 설정하지 않을 경우 기본 프로필이 설정됩니다.")
    @PostMapping("/{memberId}/profile-image")
    public ApiResponse<MemberResponseDto.CreateMemberProfileImageResultDto> createMemberProfileImage(
            @PathVariable(name = "memberId") Long memberId,
            @RequestBody @Valid MemberRequestDto.CreateMemberProfileImageDto request) {

        MemberProfileImage updatedMemberProfileImage = memberCommandService.createMemberProfileImage(memberId, request);

        return ApiResponse.onSuccess(MemberConverter.toCreateMemberProfileImageResponseDto(updatedMemberProfileImage));
    }

    @Operation(summary = "자기소개 문구 설정 API", description = "회원이 최초로 자기소개 문구를 설정하는 API입니다. 빈 문자열과 공백을 허용하지 않습니다. 최대 500자 작성 가능합니다.")
    @PatchMapping("/{memberId}/profile-introduction")
    public ApiResponse<MemberResponseDto.CreateIntroductionResultDto> createIntroduction (
            @PathVariable(name = "memberId") Long memberId,
            @RequestBody @Valid MemberRequestDto.CreateIntroductionDto request) {

        Member updatedmember= memberCommandService.createIntroduction(memberId, request);

        return ApiResponse.onSuccess(MemberConverter.toCreateIntroductionResponseDto(updatedmember));
    }
}
