package umc.duckmelang.domain.landmine.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.landmine.domain.Landmine;
import umc.duckmelang.domain.landmine.repository.LandmineRepository;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.repository.MemberRepository;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.LandmineException;
import umc.duckmelang.global.apipayload.exception.MemberException;

@Service
@RequiredArgsConstructor
public class LandmineCommandServiceImpl implements LandmineCommandService{
    private final LandmineRepository landmineRepository;
    private final MemberRepository memberRepository;

    @Override
    public Landmine addLandmine(Long memberId, String content){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        Landmine newLandmine = Landmine.builder()
                .content(content)
                .member(member)
                .build();
        return landmineRepository.save(newLandmine);
    }

    @Override
    public void removeLandmine(Long memberId, Long landmineId){
        Landmine landmine = landmineRepository.findByIdAndMemberId(landmineId, memberId)
                .orElseThrow(()-> new LandmineException(ErrorStatus.INVALID_LANDMINE));
        landmineRepository.delete(landmine);
    }
}
