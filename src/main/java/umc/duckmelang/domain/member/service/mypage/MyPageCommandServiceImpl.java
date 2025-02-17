package umc.duckmelang.domain.member.service.mypage;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.member.converter.MemberFilterConverter;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.dto.MemberFilterDto;
import umc.duckmelang.domain.member.dto.MyPageRequestDto;
import umc.duckmelang.domain.member.repository.MemberRepository;
import umc.duckmelang.domain.memberprofileimage.service.MemberProfileImageCommandService;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.MemberException;

@Service
@RequiredArgsConstructor
public class MyPageCommandServiceImpl implements MyPageCommandService{
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Member updateMemberProfile(Long memberId, MyPageRequestDto.UpdateMemberProfileDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        if(!member.getNickname().equals(request.getNickname())){
            if(memberRepository.existsByNickname(request.getNickname())){
                throw new MemberException(ErrorStatus.DUPLICATE_NICKNAME);
            }
        }
        member.updateProfile(request.getNickname(), request.getIntroduction());
        return memberRepository.save(member);
    }

    // 필터 조건 설정
    @Override
    @Transactional
    public MemberFilterDto.FilterResponseDto setFilter(Long memberId, MemberFilterDto.FilterRequestDto request){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));
        MemberFilterConverter.applyFilterRequest(member, request);
        return MemberFilterConverter.toFilterResponseDto(member);
    }
}
