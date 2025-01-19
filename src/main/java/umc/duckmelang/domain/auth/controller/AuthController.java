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
import umc.duckmelang.domain.refreshtoken.dto.RefreshTokenRequestDto;
import umc.duckmelang.global.apipayload.ApiResponse;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "로그인 API", description = "RefreshToken과 AccessToken을 발급합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH4004", description = "이메일과 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH4001", description = "이메일 또는 비밀번호가 잘못되었습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<AuthResponseDto.TokenResponse> login(@RequestBody AuthRequestDto.LoginDto request) {
        return ApiResponse.onSuccess(authService.login(request.getEmail(), request.getPassword()));
    }

    @PostMapping("/token/refresh")
    @Operation(summary = "토큰 재발급 API", description = "유효한 refreshToken을 사용해 AccessToken을 재발급합니다.")
    public ApiResponse<AuthResponseDto.TokenResponse> refreshToken(
            @Parameter(description = "유효한 Refresh Token을 입력하세요", required = true, example = "Bearer {refreshToken}")
            @RequestBody RefreshTokenRequestDto requestDto){
        return ApiResponse.onSuccess(authService.reissue(requestDto.getRefreshToken()));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 API", description = "AccessToken 블랙리스트 처리 및 RefreshToken 삭제")
    public ApiResponse<String> logout(@RequestBody RefreshTokenRequestDto requestDto) {
        authService.logout(requestDto.getRefreshToken());
        return ApiResponse.onSuccess("로그아웃되었습니다.");
    }
}