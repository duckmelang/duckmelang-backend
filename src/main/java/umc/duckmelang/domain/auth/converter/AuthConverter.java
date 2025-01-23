package umc.duckmelang.domain.auth.converter;

import org.springframework.stereotype.Component;
import umc.duckmelang.domain.auth.dto.AuthResponseDto;
import umc.duckmelang.domain.member.domain.Member;

@Component
public class AuthConverter {

    public static AuthResponseDto.LoginResponse toLoginResponse(Member member, String accessToken, String refreshToken, String provider) {
        return AuthResponseDto.LoginResponse.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .provider(provider)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
