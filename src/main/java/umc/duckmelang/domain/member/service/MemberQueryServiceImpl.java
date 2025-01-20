package umc.duckmelang.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.repository.MemberRepository;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.handler.MemberHandler;

@Service
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public Member getMemberById(long id) {
        return memberRepository.findById(id).orElseThrow(()->new MemberHandler(ErrorStatus.NO_SUCH_MEMBER));
    }

    @Override
    public boolean existsById(Long memberId) {
        return memberRepository.existsById(memberId);
    }
}
