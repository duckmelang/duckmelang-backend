package umc.duckmelang.global.auth.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import umc.duckmelang.domain.member.domain.Member;

import java.util.Collection;
import java.util.Collections;

@Getter
@AllArgsConstructor
public class MemberPrincipal implements UserDetails{

    private final Member member;

    @Override
    public String getUsername(){
        return member.getEmail();
    }

    @Override
    public String getPassword(){
        return member.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
