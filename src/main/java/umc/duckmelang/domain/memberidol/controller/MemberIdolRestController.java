package umc.duckmelang.domain.memberidol.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.duckmelang.domain.idolcategory.converter.IdolCategoryConverter;
import umc.duckmelang.domain.idolcategory.domain.IdolCategory;
import umc.duckmelang.domain.idolcategory.dto.IdolCategoryResponseDto;
import umc.duckmelang.domain.idolcategory.service.IdolCategoryQueryService;
import umc.duckmelang.domain.memberidol.converter.MemberIdolConverter;
import umc.duckmelang.domain.memberidol.domain.MemberIdol;
import umc.duckmelang.domain.memberidol.dto.MemberIdolResponseDto;
import umc.duckmelang.domain.memberidol.service.MemberIdolCommandService;
import umc.duckmelang.domain.memberidol.service.MemberIdolQueryService;
import umc.duckmelang.global.apipayload.ApiResponse;
import umc.duckmelang.global.security.user.CustomUserDetails;

import java.util.List;

@RestController
@RequestMapping("/mypage/idols")
@RequiredArgsConstructor
public class MemberIdolRestController {
    private final MemberIdolQueryService memberIdolQueryService;
    private final MemberIdolCommandService memberIdolCommandService;
    private final IdolCategoryQueryService idolCategoryQueryService;

    @Operation(summary = "현재 관심 아이돌 목록 조회 API", description = "현재 내가 설정한 관심 있는 아이돌 목록을 조회합니다. memberId에 해당하는 부분은 추후에 변경 예정입니다.")
    @GetMapping("")
    public ApiResponse<MemberIdolResponseDto.IdolListDto> getIdolList(@AuthenticationPrincipal CustomUserDetails userDetails){
        List<MemberIdol> memberIdolList = memberIdolQueryService.getIdolListByMember(userDetails.getMemberId());
        return ApiResponse.onSuccess(MemberIdolConverter.toIdolListDto(memberIdolList));
    }

    @Operation(summary = "관심 아이돌 삭제 API", description = "관심 아이돌 목록에서 관심 아이돌을 삭제하는 API입니다.")
    @DeleteMapping("/{idolId}")
    public ApiResponse<String> deleteMemberIdol(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("idolId") Long idolId){
        memberIdolCommandService.deleteMemberIdol(userDetails.getMemberId(), idolId);
        return ApiResponse.onSuccess("해당 아이돌을 삭제했습니다.");
    }

    @Operation(summary = "아이돌 목록 검색 API", description = "키워드를 통해 관심있는 아이돌을 찾는 API입니다.")
    @GetMapping("/search")
    public ApiResponse<IdolCategoryResponseDto.IdolListDto> getIdolListByKeyword(@RequestParam("keyword") String keyword){
        List<IdolCategory> idolCategoryList = idolCategoryQueryService.getIdolListByKeyword(keyword);
        return ApiResponse.onSuccess(IdolCategoryConverter.toIdolListDto(idolCategoryList));
    }

    @Operation(summary = "관심 아이돌 추가 API", description = "관심 아이돌을 추가하는 API입니다.")
    @PostMapping("/{idolId}")
    public ApiResponse<MemberIdolResponseDto.IdolDto> addMemberIdol(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("idolId") Long idolId){
        return ApiResponse.onSuccess(MemberIdolConverter.toIdolDto(memberIdolCommandService.addMemberIdol(userDetails.getMemberId(), idolId)));
    }
}
