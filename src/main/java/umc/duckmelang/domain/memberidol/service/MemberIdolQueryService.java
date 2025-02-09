package umc.duckmelang.domain.memberidol.service;

import umc.duckmelang.domain.memberidol.domain.MemberIdol;

import java.util.List;

public interface MemberIdolQueryService {
    List<MemberIdol> getIdolListByMember(Long memberId);
}
