package umc.duckmelang.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import umc.duckmelang.global.apipayload.exception.TokenException;
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

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Member member = userDetails.getMember();

        // 토큰 발급
        String accessToken = jwtUtil.generateAccessToken(member.getId());
        String refreshToken = jwtUtil.generateRefreshToken(member.getId());

        authService.saveOrUpdateRefreshToken(member, ProviderKind.LOCAL, refreshToken);
        return ApiResponse.onSuccess(new AuthResponseDto.TokenResponse(accessToken, refreshToken));
    }

    @Operation(summary = "RefreshToken으로 새롭게 토큰을 발급받는 API", description = "Refresh Token을 사용하여 새로운 Access Token과 Refresh Token을 발급받습니다.")
    @PostMapping("/token")
    public ApiResponse<AuthResponseDto.TokenResponse> newToken(
            @RequestHeader("Authorization") String authorizationHeader){

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new TokenException(ErrorStatus.TOKEN_NOT_FOUND);
        }

        String refreshToken = authorizationHeader.substring(7);
        jwtUtil.validateToken(refreshToken);
        Long memberId = jwtUtil.getMemberIdFromToken(refreshToken);

        Auth auth = authRepository.findByMemberId(memberId)
                .orElseThrow(() -> new TokenException(ErrorStatus.INVALID_TOKEN));
        if (!auth.getRefreshToken().equals(refreshToken)) {
            throw new TokenException(ErrorStatus.INVALID_TOKEN);
        }

        // 새로운 토큰 생성
        String newAccessToken = jwtUtil.generateAccessToken(memberId);
        String newRefreshToken = jwtUtil.generateRefreshToken(memberId);
        authService.saveOrUpdateRefreshToken(auth.getMember(), ProviderKind.LOCAL, newRefreshToken);

        return ApiResponse.onSuccess(new AuthResponseDto.TokenResponse(newAccessToken, newRefreshToken));
    }
}