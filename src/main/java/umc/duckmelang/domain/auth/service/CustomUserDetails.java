package umc.duckmelang.domain.auth.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import umc.duckmelang.domain.member.domain.Member;

import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Member member;

    @Override
    public Collection<?extends GrantedAuthority> getAuthorities(){return null;}
    @Override
    public String getPassword(){return member.getPassword();}
    @Override
    public String getUsername(){return member.getEmail();}
    public boolean isAccountNotExpired(){ return true; }
    public boolean isAccountNotLocked(){ return true; }
    @Override
    public boolean isCredentialsNonExpired(){ return true; }
    @Override
    public boolean isEnabled(){ return true; }
}
