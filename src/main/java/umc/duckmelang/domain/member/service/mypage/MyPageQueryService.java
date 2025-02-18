package umc.duckmelang.domain.member.service.mypage;

import umc.duckmelang.domain.member.dto.MemberFilterDto;
import umc.duckmelang.domain.member.dto.MyPageResponseDto;

public interface MyPageQueryService {
    MyPageResponseDto.MyPageProfileEditBeforeDto getMemberProfileBeforeEdit(Long memberId);
    MemberFilterDto.FilterResponseDto getMemberFilter(Long memberId);
    MyPageResponseDto.LoginInfoDto getLoginInfo(Long memberId);
}
