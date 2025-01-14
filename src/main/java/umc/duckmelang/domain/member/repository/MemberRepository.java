package umc.duckmelang.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.duckmelang.domain.member.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}
