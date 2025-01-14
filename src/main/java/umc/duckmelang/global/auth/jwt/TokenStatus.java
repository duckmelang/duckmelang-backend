package umc.duckmelang.global.auth.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TokenStatus {
    AUTHENTICATED, // 인증 성공
    EXPIRED,       // 만료됨
    INVALID        // 잘못된 토큰
}