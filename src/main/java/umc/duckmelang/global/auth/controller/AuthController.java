package umc.duckmelang.global.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import umc.duckmelang.global.apipayload.ApiResponse;
import umc.duckmelang.global.auth.dto.AuthRequestDto;
import umc.duckmelang.global.auth.dto.AuthResponseDto;
import umc.duckmelang.global.auth.jwt.JwtService;
import umc.duckmelang.global.auth.jwt.MemberPrincipal;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Operation(summary = "로그인 API", description = "RefreshToken과 AccessToken을 발급합니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDto.TokenResponse>> login(@RequestBody AuthRequestDto.LoginDto request) {

        // 이메일과 비밀번호로 인증 시도
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 인증 성공 시 사용자 정보 가져오기
        MemberPrincipal memberPrincipal = (MemberPrincipal) authentication.getPrincipal();

        // Access & Refresh 발급
        String accessToken = jwtService.createAccessToken(memberPrincipal);
        String refreshToken = jwtService.createRefreshToken(memberPrincipal);

        return ResponseEntity.ok(ApiResponse.onSuccess(
                new umc.duckmelang.global.auth.dto.AuthResponseDto.TokenResponse(accessToken, refreshToken)
        ));
    }
}
