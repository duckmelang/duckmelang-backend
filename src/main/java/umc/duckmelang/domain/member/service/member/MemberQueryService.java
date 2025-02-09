package umc.duckmelang.domain.member.service.member;

import umc.duckmelang.domain.member.domain.Member;

import java.util.Optional;

public interface MemberQueryService {
    boolean existsById(Long memberId);
    Optional<Member> findById(Long memberId);
}
