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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import umc.duckmelang.domain.auth.domain.Auth;
import umc.duckmelang.domain.auth.dto.AuthRequestDto;
import umc.duckmelang.domain.auth.dto.AuthResponseDto;
import umc.duckmelang.domain.auth.enums.ProviderKind;
import umc.duckmelang.domain.auth.repository.AuthRepository;
import umc.duckmelang.domain.auth.service.AuthService;
import umc.duckmelang.domain.auth.service.CustomUserDetails;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.global.apipayload.ApiResponse;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.GeneralException;
import umc.duckmelang.global.common.jwt.JwtUtil;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AuthService authService;
    private final AuthRepository authRepository;

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

    @Operation(summary = "RefreshToken으로 새롭게 토큰을 발급받는 API", description = "Refresh Token을 사용하여 새로운 Access Token과 Refresh Token을 발급받습니다.")
    @PostMapping("/token")
    public ApiResponse<AuthResponseDto.TokenResponse> newToken(
            @RequestHeader("Authorization") String authorizationHeader){

        // Authorization 헤더가 없는 경우
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ApiResponse.onFailure("INVALID_TOKEN", "유효하지 않은 토큰입니다.", null);
        }

        String refreshToken = authorizationHeader.substring(7); // 'Bearer ' 제거하고 토큰만 추출

        // 토큰 검증
        if (!jwtUtil.validateToken(refreshToken)) {
            return ApiResponse.onFailure("TOKEN_EXPIRED", "토큰이 만료되었습니다.", null);
        }

        String email = jwtUtil.getEmailFromToken(refreshToken);

        // Refresh Token을 기반으로 사용자 정보 조회
        Auth auth = authRepository.findByMemberEmail(email)
                .orElseThrow(() -> new RuntimeException("토큰과 연관된 사용자를 찾을 수 없습니다."));

        // 토큰이 다를 경우
        if (!auth.getRefreshToken().equals(refreshToken)) {
            return ApiResponse.onFailure("INVALID_TOKEN", "올바르지 않은 토큰입니다.", null);
        }

        // 새로운 Access Token 및 Refresh Token 생성
        String newAccessToken = jwtUtil.generateAccessToken(email);
        String newRefreshToken = jwtUtil.generateRefreshToken(email);

        // 새로운 Refresh Token 저장
        authService.saveOrUpdateRefreshToken(auth.getMember(), ProviderKind.LOCAL, newRefreshToken);
        return ApiResponse.onSuccess(new AuthResponseDto.TokenResponse(newAccessToken, newRefreshToken));
    }
}