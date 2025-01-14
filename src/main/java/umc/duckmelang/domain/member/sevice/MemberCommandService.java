package umc.duckmelang.domain.member.sevice;

import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.dto.MemberRequestDto;

public interface MemberCommandService {
    Member signupMember(MemberRequestDto.SignupDto request);
}
