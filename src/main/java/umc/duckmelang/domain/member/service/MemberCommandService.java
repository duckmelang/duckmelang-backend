package umc.duckmelang.domain.member.service;

import umc.duckmelang.domain.landmine.domain.Landmine;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.dto.MemberRequestDto;
import umc.duckmelang.domain.member.dto.MemberSignUpDto;
import umc.duckmelang.domain.memberevent.domain.MemberEvent;
import umc.duckmelang.domain.memberidol.domain.MemberIdol;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;

import java.util.List;

public interface MemberCommandService {
    Member signupMember(MemberSignUpDto.SignupDto request);
    List<MemberIdol> selectIdols(Long memberId, MemberRequestDto.SelectIdolsDto request);
    List<MemberEvent> selectEvents(Long memberId, MemberRequestDto.SelectEventsDto request);
    List<Landmine> createLandmines(Long memberId, MemberRequestDto.CreateLandminesDto request);
    MemberProfileImage createMemberProfileImage(Long memberId, MemberRequestDto.CreateMemberProfileImageDto request);
    Member createIntroduction(Long memberId, MemberRequestDto.CreateIntroductionDto request);
    Member updateMemberProfile(Long memberId, MemberRequestDto.UpdateMemberProfileDto request);
}
