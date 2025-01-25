package umc.duckmelang.domain.auth.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.duckmelang.domain.member.domain.Member;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthAttributes {
    private String nameAttributeKey;
    private String email;
    private String nickname;

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("kakao".equals(registrationId)) {
            return ofKakao(userNameAttributeName, attributes);
        } else if ("google".equals(registrationId)) {
            return ofGoogle(userNameAttributeName, attributes);
        }
        throw new IllegalArgumentException("지원하지 않는 OAuth Provider입니다.");
    }

    // 카카오 로직
    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        String nickname = profile != null ? (String) profile.get("nickname") : null;
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .email((String) kakaoAccount.get("email"))
                .nickname(nickname)
                .build();
    }

    // 구글 로직
    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .email((String) attributes.get("email"))
                .nickname((String) attributes.get("name"))
                .build();
    }

    public Member toEntity(String providerId) {
        return Member.builder()
                .email(email)
                .name(nickname)
                .password("SOCIAL_LOGIN")
                .build();
    }
}
