package umc.duckmelang.domain.memberprofileimage.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.repository.MemberRepository;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.repository.MemberProfileImageRepository;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.handler.MemberHandler;
import umc.duckmelang.global.apipayload.exception.handler.MemberProfileImageHandler;

@Service
@RequiredArgsConstructor
public class MemberProfileImageCommandServiceImpl implements MemberProfileImageCommandService {

    private final MemberProfileImageRepository memberProfileImageRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void deleteProfileImage(Long memberId, Long imageId) {
        // 회원 조회 및 유효성 검증
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 프로필 이미지 조회 및 유효성 검증
        MemberProfileImage profileImage = memberProfileImageRepository.findById(imageId)
                .orElseThrow(() -> new MemberProfileImageHandler(ErrorStatus.MEMBERPROFILEIMAGE_NOT_FOUND));

        memberProfileImageRepository.delete(profileImage);
    }

}
