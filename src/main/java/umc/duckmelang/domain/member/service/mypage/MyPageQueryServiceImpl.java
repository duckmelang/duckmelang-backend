package umc.duckmelang.domain.member.service.mypage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.member.converter.MemberProfileConverter;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.dto.MyPageResponseDto;
import umc.duckmelang.domain.member.repository.MemberRepository;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.service.MemberProfileImageQueryService;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.MemberException;

@Service
@RequiredArgsConstructor
public class MyPageQueryServiceImpl implements MyPageQueryService{
    private final MemberRepository memberRepository;
    private final MemberProfileImageQueryService memberProfileImageQueryService;

    public MyPageResponseDto.MyPageMemberProfileEditBeforeDto getMemberProfileBeforeEdit(Long memberId){
        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        // 최신 프로필 이미지 조회
        MemberProfileImage profileImage = memberProfileImageQueryService.getLatestPublicMemberProfileImage(memberId)
                .orElse(null);  // 프로필 이미지가 없을 수도 있음

        // DTO 변환 및 반환
        return MemberProfileConverter.toMemberProfileEditBeforeDto(member, profileImage);
    }
}
