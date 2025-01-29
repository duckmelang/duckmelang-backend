package umc.duckmelang.domain.auth.dto;

import lombok.*;
import umc.duckmelang.domain.member.domain.Member;

import java.time.LocalDateTime;

public class AuthResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenResponse {
        private String accessToken;
        private String refreshToken;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResponse {
        private Long memberId;
        private String email;
        private String provider;
        private String accessToken;
        private String refreshToken;
        private boolean isProfileComplete;
    }
}
