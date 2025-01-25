package umc.duckmelang.domain.auth.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import umc.duckmelang.domain.auth.dto.AuthResponseDto;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.repository.MemberRepository;
import umc.duckmelang.global.apipayload.ApiResponse;
import umc.duckmelang.global.redis.refreshtoken.RefreshTokenService;
import umc.duckmelang.global.security.jwt.JwtTokenProvider;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        // 1) 인증 주체(Principal)로부터 사용자 정보를 꺼냄
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        // 2) Provider가 내려준 속성 전체 (카카오, 구글 등)
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 3) 어떤 Provider인지 분기, email을 추출
        //    - 카카오: attributes 안에 "kakao_account"가 존재
        //    - 구글: attributes 최상위에 "email"이 존재
        //    - 단일 Provider(Kakao)만 쓴다면 여기서 분기 없이 kakao_account만 처리해도 됨
        final String email;
        final String provider;

        if (attributes.containsKey("kakao_account")) {
            // ========== 카카오 경우 ==========
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            email = (String) kakaoAccount.get("email");
            provider = "KAKAO";
        } else {
            // ========== 구글 등 기타 ==========
            // 구글은 최상위에 "email" key가 존재
            email = (String) attributes.get("email");
            provider = "GOOGLE";
            // ※ 실제로는 registrationId를 세션/Authentication 객체에 담아서
            //   정확히 "구글"인지 판단하는 로직을 넣어도 됩니다.
        }

        // 4) 이메일이 없으면 에러 처리
        if (email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email not provided by OAuth2 provider.");
            return;
        }

        // 이미 CustomOAuth2UserService 에서 사용자 생성/업데이트가 완료되었음
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User with email " + email + " not found in database."));

        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.generateAccessToken(member.getId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(member.getId());
        refreshTokenService.saveRefreshToken(refreshToken, member.getId());

        // LoginResponse 생성
        AuthResponseDto.LoginResponse loginResponse = AuthResponseDto.LoginResponse.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .provider(provider)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        // 공통 응답 생성
        ApiResponse<AuthResponseDto.LoginResponse> apiResponse = ApiResponse.onSuccess(loginResponse);

        // JSON 응답 반환
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
    }
}

