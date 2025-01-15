package umc.duckmelang.domain.member.service;

import umc.duckmelang.domain.member.dto.MemberRequestDto;
import umc.duckmelang.domain.memberevent.domain.MemberEvent;
import umc.duckmelang.domain.memberidol.domain.MemberIdol;

import java.util.List;

public interface MemberCommandService {
    List<MemberIdol> selectIdols(Long memberId, MemberRequestDto.SelectIdolsDto request);
    List<MemberEvent> selectEvents(Long memberId, MemberRequestDto.SelectEventsDto request);
}
