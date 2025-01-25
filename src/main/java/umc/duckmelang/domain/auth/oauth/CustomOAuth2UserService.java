package umc.duckmelang.domain.auth.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.auth.enums.ProviderKind;
import umc.duckmelang.domain.auth.repository.AuthRepository;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.repository.MemberRepository;
import umc.duckmelang.domain.auth.domain.Auth;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;

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
        Member existingMember = memberRepository.findByEmail(attributes.getEmail()).orElse(null);
        if (existingMember != null) {
            if (attributes.getNickname() != null && !attributes.getNickname().isEmpty()) {
                existingMember.setName(attributes.getNickname());}
            if (!existingMember.getEmail().equals(attributes.getEmail())) {
                existingMember.setEmail(attributes.getEmail());}
            return memberRepository.save(existingMember);
        }

        Auth existingAuth = authRepository.findByTextIdAndProvider(attributes.getEmail(), ProviderKind.valueOf(providerId.toUpperCase())).orElse(null);
        if (existingAuth != null) {
            authRepository.save(existingAuth);
            return existingAuth.getMember();}

        Auth auth = authRepository.findByTextIdAndProvider(attributes.getEmail(), ProviderKind.valueOf(providerId.toUpperCase()))
                .orElseGet(() -> {
                    Member newMember = attributes.toEntity(providerId);
                    Member savedMember = memberRepository.save(newMember);

                    Auth newAuth = Auth.builder()
                            .textId(attributes.getEmail())
                            .provider(ProviderKind.valueOf(providerId.toUpperCase()))
                            .member(savedMember)
                            .build();
                    authRepository.save(newAuth);
                    return newAuth;
                });
        Member member = auth.getMember();
        return member;
    }
}


