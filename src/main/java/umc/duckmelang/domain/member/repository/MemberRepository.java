package umc.duckmelang.domain.member.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import umc.duckmelang.domain.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
