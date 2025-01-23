package umc.duckmelang.domain.auth.social.kakao;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import umc.duckmelang.domain.auth.converter.AuthConverter;
import umc.duckmelang.domain.auth.dto.AuthResponseDto;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.global.apipayload.ApiResponse;

@RestController
@RequiredArgsConstructor
public class KakaoController {
    private final KakaoService kakaoService;

    @GetMapping("/login/oauth/kakao")
    @Operation(summary = "카카오 로그인 API", description = "인가 코드를 받아 사용자 정보를 처리하고 토큰을 발급합니다.")
    public ApiResponse<AuthResponseDto.LoginResponse> kakaoLogin(@RequestParam("code") String accessCode, HttpServletResponse response){
        AuthResponseDto.ServiceLoginResult loginResult = kakaoService.login(accessCode, response);
        return ApiResponse.onSuccess(
                AuthConverter.toLoginResponse(
                        loginResult.getMember(),
                        loginResult.getAccessToken(),
                        loginResult.getRefreshToken(),
                        "KAKAO"
        ));
    }
}
