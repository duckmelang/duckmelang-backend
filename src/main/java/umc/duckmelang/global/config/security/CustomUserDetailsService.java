package umc.duckmelang.global.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 하드코딩된 더미 사용자 데이터
        if("testuser".equals(username)){
            return User.builder()
                    .username("testuser")
                    .password("password")
                    .build();
        }
        throw new UsernameNotFoundException("사용자를 찾을 수 없습니다." + username);
    }
}
