package umc.duckmelang.domain.member.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.duckmelang.domain.application.service.ApplicationQueryService;
import umc.duckmelang.domain.member.converter.MemberConverter;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.dto.MemberResponseDto;
import umc.duckmelang.domain.member.service.MemberQueryService;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.service.MemberProfileImageQueryService;
import umc.duckmelang.domain.post.service.PostQueryService;
import umc.duckmelang.domain.postimage.service.PostImageQueryService;

@Service
@RequiredArgsConstructor
public class ProfileFacadeService {
    private final MemberQueryService memberQueryService;
    private final PostQueryService postQueryService;
    private final PostImageQueryService postImageQueryService;
    private final ApplicationQueryService applicationService;
    private final MemberProfileImageQueryService profileImageService;

    @Transactional(readOnly = true)
    public MemberResponseDto.OtherProfileDto getOtherProfileByMemberId(Long memberId, int page) {
        // 회원 기본 정보 조회
        Member member = memberQueryService.getMemberById(memberId);

        // 포스트 수 조회
        int postCount = postQueryService.getPostCount(memberId);

        // 매칭 수 조회
        int matchCount = applicationService.countMatchedApplications(memberId);

        // 프로필 이미지 1개 조회
        MemberProfileImage image = profileImageService.findLatestOneByMemberId(memberId);

        return MemberConverter.ToOtherProfileDto(member, postCount, matchCount, image);
    }
}