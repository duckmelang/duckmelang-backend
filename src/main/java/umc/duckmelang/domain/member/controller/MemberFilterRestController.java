package umc.duckmelang.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.duckmelang.domain.member.dto.MemberFilterDto;
import umc.duckmelang.domain.member.service.mypage.MyPageCommandService;
import umc.duckmelang.domain.member.service.mypage.MyPageQueryService;
import umc.duckmelang.global.apipayload.ApiResponse;
import umc.duckmelang.global.security.user.CustomUserDetails;

@RestController
@RequestMapping("/mypage/filters")
@RequiredArgsConstructor
public class MemberFilterRestController {
    private final MyPageQueryService myPageQueryService;
    private final MyPageCommandService myPageCommandService;

    @Operation(summary = "필터 조건 조회 API", description = "사용자가 설정한 필터 조건을 조회하는 API입니다.")
    @GetMapping("")
    public ApiResponse<MemberFilterDto.FilterResponseDto> getMemberFilter(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ApiResponse.onSuccess(myPageQueryService.getMemberFilter(userDetails.getMemberId()));
    }

    @Operation(summary = "필터 조건 설정 API", description = "사용자가 필터 조건을 설정하는 API입니다. 성별이나 나이 둘 중 하나만 선택할 수도, 전부 다 선택할 수도 있습니다.")
    @PostMapping("")
    public ApiResponse<MemberFilterDto.FilterResponseDto> setFilter(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                    @RequestBody MemberFilterDto.FilterRequestDto request) {
        return ApiResponse.onSuccess(myPageCommandService.setFilter(userDetails.getMemberId(), request));
    }
}
