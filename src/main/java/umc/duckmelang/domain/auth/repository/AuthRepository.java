package umc.duckmelang.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.duckmelang.domain.auth.domain.Auth;
import umc.duckmelang.domain.member.domain.Member;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    // Member를 기반으로 Auth 정보 조회
    Optional<Auth> findByMember(Member member);
    Optional<Auth> findByMemberEmail(String email);
}
