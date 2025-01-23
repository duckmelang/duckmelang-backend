package umc.duckmelang.domain.auth.social.kakao;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.auth.domain.Auth;
import umc.duckmelang.domain.auth.dto.AuthResponseDto;
import umc.duckmelang.domain.auth.enums.ProviderKind;
import umc.duckmelang.domain.auth.repository.AuthRepository;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.repository.MemberRepository;
import umc.duckmelang.global.redis.refreshtoken.RefreshTokenService;
import umc.duckmelang.global.security.jwt.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class KakaoService {
    private final KakaoUtil kakaoUtil;
    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    public AuthResponseDto.ServiceLoginResult login(String accessCode, HttpServletResponse httpServletResponse) {
        // 1. Access Token 요청
        KakaoDto.OAuthToken oAuthToken = kakaoUtil.requestToken(accessCode);

        // 2. 사용자 이메일 요청
        KakaoDto.KakaoProfile kakaoProfile = kakaoUtil.requestProfile(oAuthToken.getAccess_token());

        // 3. 사용자 이메일 확인
        String email = kakaoProfile.getKakao_account().getEmail();
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("이메일 정보가 없습니다.");
        }

        // 4. DB에서 사용자 검색 또는 생성
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(
                        Member.builder()
                                .email(email)
                                .name("카카오 사용자") // 기본값으로 이름 설정
                                .password("SOCIAL_LOGIN") // 소셜 로그인 비밀번호 기본값
                                .build()
                ));

        // 5. Auth 테이블에 해당 사용자의 인증 정보 저장
        authRepository.findByTextIdAndProvider(email, ProviderKind.KAKAO)
                .orElseGet(() -> authRepository.save(
                        Auth.builder()
                                .textId(email)
                                .provider(ProviderKind.KAKAO)
                                .member(member)
                                .build()
                ));

        // 6. Access Token 및 Refresh Token 생성
        String accessToken = jwtTokenProvider.generateAccessToken(member.getId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(member.getId());

        // 7. Refresh Token을 Redis에 저장
        refreshTokenService.saveRefreshToken(refreshToken, member.getId());

        // 8. HTTP 응답 헤더에 Access Token추가
        httpServletResponse.addHeader("Authorization", "Bearer " + accessToken);

        return new AuthResponseDto.ServiceLoginResult(member, accessToken, refreshToken);
    }
}
