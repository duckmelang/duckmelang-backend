package umc.duckmelang.domain.memberidol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.memberidol.domain.MemberIdol;

@Repository
public interface MemberIdolRepository extends JpaRepository<MemberIdol, Long> {
    void deleteAllByMember(Member member);
}
