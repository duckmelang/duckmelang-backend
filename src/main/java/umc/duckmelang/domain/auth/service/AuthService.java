package umc.duckmelang.domain.auth.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.auth.domain.Auth;
import umc.duckmelang.domain.auth.enums.ProviderKind;
import umc.duckmelang.domain.auth.repository.AuthRepository;
import umc.duckmelang.domain.member.domain.Member;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;

    // 로그인 시 Refresh Token을 저장하거나 갱신
    @Transactional
    public void saveOrUpdateRefreshToken(Member member, ProviderKind providerKind, String refreshToken){
        // 사용자와 관련된 Auth 정보 조회
        Auth auth = authRepository.findByMember(member)
                .orElse(Auth.builder()
                        .member(member)
                        .provider(providerKind)
                        .build());

        // Refresh Token 갱신
        auth.setRefreshToken(refreshToken);
        authRepository.save(auth);
    }

    // 저장된 Refresh Token 검증
    @Transactional(readOnly = true)
    public boolean validateRefreshToken(Member member, String refreshToken) {
        return authRepository.findByMember(member)
                .map(auth -> auth.getRefreshToken().equals(refreshToken)) // 토큰 일치 여부 확인
                .orElse(false);
    }
}
