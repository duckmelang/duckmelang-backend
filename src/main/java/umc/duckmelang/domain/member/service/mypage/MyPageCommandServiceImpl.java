package umc.duckmelang.domain.member.service.mypage;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.dto.MyPageRequestDto;
import umc.duckmelang.domain.member.repository.MemberRepository;
import umc.duckmelang.domain.memberprofileimage.service.MemberProfileImageCommandService;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.MemberException;

@Service
@RequiredArgsConstructor
public class MyPageCommandServiceImpl implements MyPageCommandService{
    private final MemberRepository memberRepository;
    private final MemberProfileImageCommandService memberProfileImageCommandService;

    @Override
    @Transactional
    public Member updateMemberProfile(Long memberId, MyPageRequestDto.UpdateMemberProfileDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));
        member.updateProfile(request.getNickname(), request.getIntroduction());
        return memberRepository.save(member);
    }
}
