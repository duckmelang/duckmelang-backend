package umc.duckmelang.domain.member.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.duckmelang.domain.application.service.ApplicationQueryService;
import umc.duckmelang.domain.member.converter.MemberProfileConverter;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.dto.MyPageResponseDto;
import umc.duckmelang.domain.member.service.member.MemberQueryService;
import umc.duckmelang.domain.member.service.mypage.MyPageCommandService;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.service.MemberProfileImageCommandService;
import umc.duckmelang.domain.memberprofileimage.service.MemberProfileImageQueryService;
import umc.duckmelang.domain.post.service.PostQueryService;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.MemberException;
import umc.duckmelang.global.apipayload.exception.MemberProfileImageException;

@Service
@RequiredArgsConstructor
public class ProfileFacadeService {
    private final MyPageCommandService myPageCommandService;
    private final MemberQueryService memberQueryService;
    private final PostQueryService postQueryService;
    private final ApplicationQueryService applicationService;
    private final ApplicationQueryService applicationQueryService;
    private final MemberProfileImageQueryService memberProfileImageQueryService;
    private final MemberProfileImageCommandService memberProfileImageCommandService;

    @Transactional(readOnly = true)
    public MyPageResponseDto.MypageMemberPreviewResultDto getMypageMemberPreview(Long memberId) {
        // 회원 기본 정보 조회
        Member member = memberQueryService.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        // 대표 프로필 이미지 1개 조회
        MemberProfileImage latestPublicMemberProfileImage = memberProfileImageQueryService.getLatestPublicMemberProfileImage(memberId)
                .orElseThrow(() -> new MemberProfileImageException(ErrorStatus.MEMBER_PROFILE_IMAGE_NOT_FOUND));

        return MemberProfileConverter.toGetMemberPreviewResponseDto(member, latestPublicMemberProfileImage);
    }

    @Transactional(readOnly = true)
    public MyPageResponseDto.OtherProfileDto getOtherProfileByMemberId(Long memberId, int page) {
        // 회원 기본 정보 조회
        Member member = memberQueryService.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        // 포스트 수 조회
        int postCount = postQueryService.getPostCount(memberId);

        // 매칭 수 조회
        int matchCount = applicationService.countMatchedApplications(memberId);

        // 프로필 이미지 1개 조회
        MemberProfileImage image = memberProfileImageQueryService.getLatestPublicMemberProfileImage(memberId)
                .orElseThrow(() -> new MemberProfileImageException(ErrorStatus.MEMBER_PROFILE_IMAGE_NOT_FOUND));

        return MemberProfileConverter.ToOtherProfileDto(member, postCount, matchCount, image);
    }

    @Transactional(readOnly = true)
    public MyPageResponseDto.MypageMemberProfileResultDto getMyProfileByMemberId(Long memberId) {

        // 회원 기본 정보 조회
        Member member = memberQueryService.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        // 포스트 수 조회
        int postCount = postQueryService.getPostCount(memberId);

        // 매칭 수 조회
        int matchCount = applicationQueryService.countMatchedApplications(memberId);

        // 대표 프로필 이미지 1개 조회
        MemberProfileImage image = memberProfileImageQueryService.getLatestPublicMemberProfileImage(memberId)
                .orElseThrow(() -> new MemberProfileImageException(ErrorStatus.MEMBER_PROFILE_IMAGE_NOT_FOUND));

        return MemberProfileConverter.toGetMemberProfileResponseDto(member, image, postCount, matchCount);
    }
}