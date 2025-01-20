package umc.duckmelang.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.duckmelang.domain.member.Facade.ProfileFacadeService;
import umc.duckmelang.domain.member.dto.MemberResponseDto;
import umc.duckmelang.global.apipayload.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/")
public class MemberRestController {
    private final ProfileFacadeService profileFacadeService;

    @GetMapping(path = "/{memberId}")
    @Operation(summary = "프로필 조회",description = "path variable로 프로필을 확인하고자하는 상대 member의 id를 받습니다.")
    ApiResponse<MemberResponseDto.OtherProfileDto> getOtherProfile(@PathVariable Long memberId, int page) {
        return ApiResponse.onSuccess(profileFacadeService.getOtherProfileByMemberId(memberId, page));
    }
}
