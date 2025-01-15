package umc.duckmelang.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.duckmelang.domain.landmine.domain.Landmine;
import umc.duckmelang.domain.member.converter.MemberConverter;
import umc.duckmelang.domain.member.dto.MemberRequestDto;
import umc.duckmelang.domain.member.dto.MemberResponseDto;
import umc.duckmelang.domain.member.service.MemberCommandService;
import umc.duckmelang.domain.memberevent.domain.MemberEvent;
import umc.duckmelang.domain.memberidol.domain.MemberIdol;
import umc.duckmelang.global.apipayload.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberCommandService memberCommandService;

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
    public ApiResponse<MemberResponseDto.SelectLandmineResultDto> selectLandmines(
            @PathVariable(name = "memberId") Long memberId,
            @RequestBody @Valid MemberRequestDto.SelectLandminesDto request) {

        List<Landmine> updatedLandmineList = memberCommandService.selectLandmines(memberId, request);

        // 리스트가 비어 있는 경우 허용(따라서 예외 처리 생략)

        return ApiResponse.onSuccess(MemberConverter.toSelectLandmineResponseDto(updatedLandmineList));

    }




}
