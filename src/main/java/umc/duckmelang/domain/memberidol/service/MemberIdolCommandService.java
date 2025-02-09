package umc.duckmelang.domain.memberidol.service;

import umc.duckmelang.domain.memberidol.domain.MemberIdol;

public interface MemberIdolCommandService {
    void deleteMemberIdol(Long memberId, Long idolId);
    MemberIdol addMemberIdol(Long memberId, Long idolId);
}
