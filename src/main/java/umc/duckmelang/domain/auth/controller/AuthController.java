package umc.duckmelang.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.duckmelang.domain.auth.dto.AuthRequestDto;
import umc.duckmelang.domain.auth.dto.AuthResponseDto;
import umc.duckmelang.domain.auth.service.AuthService;
import umc.duckmelang.global.apipayload.ApiResponse;
import umc.duckmelang.global.redis.blacklist.BlacklistService;
import umc.duckmelang.global.security.jwt.JwtUtil;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final BlacklistService blacklistService;

    @PostMapping("/login")
    @Operation(summary = "로그인 API", description = "RefreshToken과 AccessToken을 발급합니다.")
    public ApiResponse<AuthResponseDto.TokenResponse> login(@RequestBody AuthRequestDto.LoginDto request) {
        return ApiResponse.onSuccess(authService.login(request.getEmail(), request.getPassword()));
    }

    @PostMapping("/token/refresh")
    @Operation(summary = "토큰 재발급 API", description = "유효한 refreshToken을 사용해 AccessToken을 재발급합니다.")
    public ApiResponse<AuthResponseDto.TokenResponse> refreshToken(@RequestBody AuthRequestDto.RefreshTokenRequestDto request) {
        return ApiResponse.onSuccess(authService.reissue(request.getRefreshToken()));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 API", description = "토큰을 블랙리스트에 추가합니다.")
    public ApiResponse<String> logout(HttpServletRequest request) {
        String token = jwtUtil.extractToken(request);
        long expiration = jwtUtil.getExpirationFromToken(token);
        blacklistService.addToBlacklist(token, expiration);
        return ApiResponse.onSuccess("로그아웃되었습니다.");
    }
}