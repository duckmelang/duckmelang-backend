package umc.duckmelang.domain.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.repository.MemberRepository;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.handler.MemberHandler;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService{

    private final MemberRepository memberRepository;

    /**
     * 회원 ID로 회원의 만 나이를 계산하는 서비스 메서드
     * @param memberId 회원 ID(Long)
     * @return 현재 나이(int)
     */
    @Override
    @Transactional
    public int calculateMemberAge(Long memberId) {

        // 회원 정보 조회 및 유효성 검증
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 생년월일 가져오기
        LocalDate birth = member.getBirth();

        // 현재 날짜 가져오기
        LocalDate today = LocalDate.now();

        // 현재 연도와 태어난 연도의 차이를 계산
        int age = today.getYear() - birth.getYear();

        // 생일이 올해 아직 지나지 않았다면 나이를 1 줄임
        if (today.isBefore(birth.withYear(today.getYear()))) {
            age--;
        }
        return age;
    }
}
