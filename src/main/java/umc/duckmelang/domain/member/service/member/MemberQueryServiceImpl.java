package umc.duckmelang.domain.member.service.member;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryServiceImpl implements MemberQueryService {
    private final MemberRepository memberRepository;

    @Override
    public Optional<Member> findById(Long memberId){
        return memberRepository.findById(memberId);
    }

    @Override
    public boolean existsById(Long memberId) {
        return memberRepository.existsById(memberId);
    }
}
