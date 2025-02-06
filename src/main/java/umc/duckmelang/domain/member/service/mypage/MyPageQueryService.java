package umc.duckmelang.domain.member.service.mypage;

import umc.duckmelang.domain.member.dto.MyPageResponseDto;

public interface MyPageQueryService {
    MyPageResponseDto.MyPageMemberProfileEditBeforeDto getMemberProfileBeforeEdit(Long memberId);
}
