package umc.duckmelang.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import umc.duckmelang.domain.member.converter.MemberConverter;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.dto.MemberRequestDto;
import umc.duckmelang.domain.member.dto.MemberResponseDto;
import umc.duckmelang.domain.member.repository.MemberRepository;
import umc.duckmelang.domain.member.sevice.MemberCommandService;
import umc.duckmelang.global.apipayload.ApiResponse;
import umc.duckmelang.global.jwt.JwtService;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberCommandService memberCommandService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ApiResponse<MemberResponseDto.SignupResultDto> signup(@RequestBody @Valid MemberRequestDto.SignupDto request){
        Member member = memberCommandService.signupMember(request);
        return ApiResponse.onSuccess(MemberConverter.toSignupResultDto(member));
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<MemberResponseDto.TokenResponse >> login(@RequestBody MemberRequestDto.LoginDto request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.onFailure("INVALID_PASSWORD", "비밀번호 불일치", null)
            );
        }

        // Access & Refresh 발급
        String accessToken = jwtService.createAccessToken(member.getId());
        String refreshToken = jwtService.createRefreshToken(member.getId());

        return ResponseEntity.ok(ApiResponse.onSuccess(
                new umc.duckmelang.domain.member.dto.MemberResponseDto.TokenResponse(accessToken, refreshToken)
        ));
    }
}
