package umc.duckmelang.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import umc.duckmelang.domain.auth.dto.AuthRequestDto;
import umc.duckmelang.domain.auth.dto.AuthResponseDto;
import umc.duckmelang.domain.auth.enums.ProviderKind;
import umc.duckmelang.domain.auth.service.AuthService;
import umc.duckmelang.domain.auth.service.CustomUserDetails;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.global.apipayload.ApiResponse;
import umc.duckmelang.global.common.jwt.JwtUtil;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AuthService authService;

    @Operation(summary = "로그인 API", description = "RefreshToken과 AccessToken을 발급합니다.")
    @PostMapping("/login")
    public ApiResponse<AuthResponseDto.TokenResponse> login(@RequestBody AuthRequestDto.LoginDto request) {

        // 이메일과 비밀번호로 인증 시도
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtil.generateAccessToken(request.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(request.getEmail());

        // 로그인한 Member 객체 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Member member = userDetails.getMember(); // 올바르게 Member 객체를 가져옵니다.
        authService.saveOrUpdateRefreshToken(member, ProviderKind.LOCAL, refreshToken);
        return ApiResponse.onSuccess(new AuthResponseDto.TokenResponse(accessToken, refreshToken));
    }
}
