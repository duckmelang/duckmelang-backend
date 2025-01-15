package umc.duckmelang.domain.memberevent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.memberevent.domain.MemberEvent;

public interface MemberEventRepository extends JpaRepository<MemberEvent, Long> {
    void deleteAllByMember(Member member);
}
