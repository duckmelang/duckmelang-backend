package umc.duckmelang.domain.member.service.mypage;

import umc.duckmelang.domain.member.dto.MemberFilterDto;
import umc.duckmelang.domain.member.dto.MyPageResponseDto;

public interface MyPageQueryService {
    MyPageResponseDto.MyPagProfileEditBeforeDto getMemberProfileBeforeEdit(Long memberId);
    MemberFilterDto.FilterResponseDto getMemberFilter(Long memberId);
}
