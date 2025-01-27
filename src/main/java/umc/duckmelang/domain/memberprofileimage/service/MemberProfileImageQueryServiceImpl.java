package umc.duckmelang.domain.memberprofileimage.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.repository.MemberRepository;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.repository.MemberProfileImageRepository;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.handler.MemberHandler;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberProfileImageQueryServiceImpl implements MemberProfileImageQueryService {

    private final MemberProfileImageRepository memberProfileImageRepository;
    private final MemberRepository memberRepository;

    //대표 프로필 사진 불러오는 서비스(해당하는 사용자의 isPublic 설정이 true인 것들 중 createdAt이 가장 최신인 entity 1개)
    //회원의 프로필사진이 노출되는 다양한 API에 사용 가능하도록 별도의 Service로 작성
    @Override
    @Transactional
    public Optional<MemberProfileImage> getLatestPublicMemberProfileImage(Long memberId) {
        // 회원 조회 및 유효성 검증
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return memberProfileImageRepository.findFirstByMemberIdAndIsPublicTrueOrderByCreatedAtDesc(memberId);
    }

    @Override
    @Transactional
    public Page<MemberProfileImage> getAllMemberProfileImageByMemberId(Long memberId, int page) {
        // 회원 조회 및 유효성 검증
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return memberProfileImageRepository.findAllByMemberId(memberId, PageRequest.of(page,10));
    }


}
