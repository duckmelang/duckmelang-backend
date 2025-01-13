package umc.duckmelang.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import umc.duckmelang.domain.member.converter.MemberConverter;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.dto.MemberRequestDto;
import umc.duckmelang.domain.member.dto.MemberResponseDto;
import umc.duckmelang.domain.member.sevice.MemberCommandService;
import umc.duckmelang.global.apipayload.ApiResponse;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberCommandService memberCommandService;

    @PostMapping("/signup")
    public ApiResponse<MemberResponseDto.JoinResultDto> join(@RequestBody @Valid MemberRequestDto.JoinDto request){
        Member member = memberCommandService.joinMember(request);
        return ApiResponse.onSuccess(MemberConverter.toJoinResultDto(member));
    }

}
