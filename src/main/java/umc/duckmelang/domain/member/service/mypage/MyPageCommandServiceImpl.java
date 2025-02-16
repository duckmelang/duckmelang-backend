package umc.duckmelang.domain.member.service.mypage;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.member.converter.MemberFilterConverter;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.dto.MemberFilterDto;
import umc.duckmelang.domain.member.dto.MyPageRequestDto;
import umc.duckmelang.domain.member.repository.MemberRepository;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.MemberException;
import umc.duckmelang.global.redis.blacklist.BlacklistService;
import umc.duckmelang.global.security.jwt.JwtUtil;

@Service
@RequiredArgsConstructor
public class MyPageCommandServiceImpl implements MyPageCommandService{
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final BlacklistService blacklistService;

    @Override
    @Transactional
    public Member updateMemberProfile(Long memberId, MyPageRequestDto.UpdateMemberProfileDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));
        member.updateProfile(request.getNickname(), request.getIntroduction());
        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public MemberFilterDto.FilterResponseDto setFilter(Long memberId, MemberFilterDto.FilterRequestDto request){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));
        MemberFilterConverter.applyFilterRequest(member, request);
        return MemberFilterConverter.toFilterResponseDto(member);
    }

    @Override
    @Transactional
    public void deleteMember(Long memberId, String token){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));
        member.deleteMember();
        memberRepository.save(member);
        long expiration = jwtUtil.getExpirationFromToken(token);
        blacklistService.addToBlacklist(token, expiration);
    }
}
