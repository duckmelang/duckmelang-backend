package umc.duckmelang.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import umc.duckmelang.domain.auth.dto.AuthRequestDto;
import umc.duckmelang.domain.auth.dto.AuthResponseDto;
import umc.duckmelang.domain.auth.service.AuthService;
import umc.duckmelang.global.apipayload.ApiResponse;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "로그인 API", description = "RefreshToken과 AccessToken을 발급합니다.")
    public ApiResponse<AuthResponseDto.TokenResponse> login(@RequestBody AuthRequestDto.LoginDto request) {
        return ApiResponse.onSuccess(authService.login(request.getEmail(), request.getPassword()));
    }

    @PostMapping("/token/refresh")
    @Operation(summary = "토큰 재발급 API", description = "유효한 refreshToken을 사용해 AccessToken을 재발급합니다.")
    public ApiResponse<AuthResponseDto.TokenResponse> refreshToken(@RequestBody AuthRequestDto.RefreshTokenRequestDto requestDto){
        return ApiResponse.onSuccess(authService.reissue(requestDto.getRefreshToken()));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 API", description = "RefreshToken 삭제를 통해 로그아웃을 합니다.")
    public ApiResponse<String> logout(@RequestBody AuthRequestDto.RefreshTokenRequestDto requestDto) {
        authService.logout(requestDto.getRefreshToken());
        return ApiResponse.onSuccess("로그아웃되었습니다.");
    }
}