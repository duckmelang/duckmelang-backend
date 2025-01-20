package umc.duckmelang.domain.member.service;

import umc.duckmelang.domain.member.domain.Member;

public interface MemberQueryService {
    Member getMemberById(long id);

    boolean existsById(Long memberId);
}
