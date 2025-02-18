package umc.duckmelang.domain.auth.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.auth.domain.enums.ProviderKind;
import umc.duckmelang.domain.auth.repository.AuthRepository;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.repository.MemberRepository;
import umc.duckmelang.domain.auth.domain.Auth;
import umc.duckmelang.domain.notificationsetting.domain.NotificationSetting;
import umc.duckmelang.domain.notificationsetting.repository.NotificationSettingRepository;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;
    private final NotificationSettingRepository notificationSettingRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        OAuthAttributes authAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, attributes);
        saveOrUpdate(authAttributes, registrationId);
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                userNameAttributeName
        );
    }

    private Member saveOrUpdate(OAuthAttributes attributes, String providerId) {
        ProviderKind providerKind = ProviderKind.valueOf(providerId.toUpperCase());

        return memberRepository.findByEmail(attributes.getEmail())
                .map(member -> {
                    updateMemberInfo(member, attributes);
                    return memberRepository.save(member);
                })
                .orElseGet(() -> {
                    Auth existingAuth = authRepository.findByTextIdAndProvider(attributes.getEmail(), providerKind).orElse(null);
                    if (existingAuth != null) {
                        return existingAuth.getMember();
                    }

                    Member newMember = Member.builder()
                            .email(attributes.getEmail())
                            .password("SOCIAL_LOGIN")
                            .isProfileComplete(false)
                            .build();

                    Member savedMember = memberRepository.save(newMember);
                    Auth newAuth = Auth.builder()
                            .textId(attributes.getEmail())
                            .provider(providerKind)
                            .member(savedMember)
                            .build();
                    authRepository.save(newAuth);

                    //소셜 회원가입 시 알림 설정 자동 생성
                    NotificationSetting notificationSetting = NotificationSetting.builder()
                            .member(savedMember)
                            .chatNotificationEnabled(true)  // 기본값을 true로 설정
                            .requestNotificationEnabled(true)
                            .reviewNotificationEnabled(true)
                            .bookmarkNotificationEnabled(true)
                            .build();
                    notificationSettingRepository.save(notificationSetting);
                    savedMember.setNotificationSetting(notificationSetting);

                    return savedMember;
                });
    }

    private void updateMemberInfo(Member member, OAuthAttributes attributes) {
        if (!member.getEmail().equals(attributes.getEmail())) {
            member.setEmail(attributes.getEmail());
        }
    }
}


