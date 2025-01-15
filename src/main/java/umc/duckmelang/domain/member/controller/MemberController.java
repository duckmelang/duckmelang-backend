package umc.duckmelang.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.duckmelang.domain.member.converter.MemberConverter;
import umc.duckmelang.domain.member.dto.MemberRequestDto;
import umc.duckmelang.domain.member.dto.MemberResponseDto;
import umc.duckmelang.domain.member.service.MemberCommandService;
import umc.duckmelang.domain.memberidol.domain.MemberIdol;
import umc.duckmelang.global.apipayload.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberCommandService memberCommandService;

    @Operation(summary = "덕질하는 아이돌 선택 API", description = "회원이 덕질하는 아이돌을 선택하는 API입니다.")
    @PostMapping("/{memberId}/interesting-idol")
    public ApiResponse<MemberResponseDto.SelectIdolsResultDto> selectIdols(
            @PathVariable(name = "memberId") Long memberId,
            @RequestBody @Valid MemberRequestDto.SelectIdolsDto request) {

        List<MemberIdol> updatedMemberIdols = memberCommandService.selectIdols(memberId, request);

        // 리스트가 비어 있는 경우 예외 처리
        if (updatedMemberIdols.isEmpty()) {
            throw new IllegalArgumentException("선택된 아이돌 카테고리가 없습니다.");
        }

        return ApiResponse.onSuccess(MemberConverter.toSelectIdolResponseDto(updatedMemberIdols));


    }

}
