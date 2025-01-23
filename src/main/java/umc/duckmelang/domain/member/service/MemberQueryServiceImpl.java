package umc.duckmelang.domain.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.repository.MemberRepository;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService{

    private final MemberRepository memberRepository;

    //member 조회 서비스
    @Override
    @Transactional
    public Optional<Member> getMemberById(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
