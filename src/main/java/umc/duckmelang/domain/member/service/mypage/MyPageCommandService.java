package umc.duckmelang.domain.member.service.mypage;

import jakarta.servlet.http.HttpServletRequest;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.dto.MemberFilterDto;
import umc.duckmelang.domain.member.dto.MyPageRequestDto;

public interface MyPageCommandService {
    Member updateMemberProfile(Long memberId, MyPageRequestDto.UpdateMemberProfileDto request);
    MemberFilterDto.FilterResponseDto setFilter(Long memberId, MemberFilterDto.FilterRequestDto request);
    void deleteMember(HttpServletRequest request);
}
