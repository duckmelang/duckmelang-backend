package umc.duckmelang.domain.member.Facade;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.handler.MemberHandler;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ProfileFacadeService {
    private final MemberQueryService memberQueryService;
    private final PostQueryService postQueryService;
    private final ApplicationQueryService applicationService;
    private final MemberProfileImageQueryService profileImageService;

    @Transactional(readOnly = true)
    public MemberResponseDto.OtherProfileDto getOtherProfileByMemberId(Long memberId, int page) {
        validateMember(memberId);

        // 회원 기본 정보 조회
        Member member = memberQueryService.getMemberById(memberId);

        // 게시글 수 조회
        int postCount = postQueryService.countByMemberId(memberId);

        // 매칭 수 조회
        int matchCount = applicationService.countMatchedApplications(memberId);

        // 프로필 이미지 페이징 조회
        Page<String> imagePage = profileImageService.getMemberImages(memberId, page);

        return MemberConverter.ToOtherProfileDto(member, postCount, matchCount, imagePage);
    }

    // 예외 처리
    private void validateMember(Long memberId) {
        if (!memberQueryService.existsById(memberId)) {
            throw new MemberHandler(ErrorStatus.NO_SUCH_MEMBER);
        }
    }
}