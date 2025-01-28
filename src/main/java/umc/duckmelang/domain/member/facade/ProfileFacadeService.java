package umc.duckmelang.domain.member.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.duckmelang.domain.application.service.ApplicationQueryService;
import umc.duckmelang.domain.member.converter.MemberConverter;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.dto.MemberRequestDto;
import umc.duckmelang.domain.member.dto.MemberResponseDto;
import umc.duckmelang.domain.member.service.MemberCommandService;
import umc.duckmelang.domain.member.service.MemberQueryService;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.service.MemberProfileImageCommandService;
import umc.duckmelang.domain.memberprofileimage.service.MemberProfileImageQueryService;
import umc.duckmelang.domain.post.service.PostQueryService;
import umc.duckmelang.domain.postimage.service.PostImageQueryService;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.MemberException;
import umc.duckmelang.global.apipayload.exception.MemberProfileImageException;

@Service
@RequiredArgsConstructor
public class ProfileFacadeService {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;
    private final PostQueryService postQueryService;
    private final PostImageQueryService postImageQueryService;
    private final ApplicationQueryService applicationService;
    private final MemberProfileImageQueryService profileImageService;
    private final ApplicationQueryService applicationQueryService;
    private final MemberProfileImageQueryService memberProfileImageQueryService;
    private final MemberProfileImageCommandService memberProfileImageCommandService;


    @Transactional(readOnly = true)
    public MemberResponseDto.GetMypageMemberPreviewResultDto getMypageMemberPreview(Long memberId) {

        // 회원 기본 정보 조회
        Member member = memberQueryService.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        // 대표 프로필 이미지 1개 조회
        MemberProfileImage latestPublicMemberProfileImage = memberProfileImageQueryService.getLatestPublicMemberProfileImage(memberId)
                .orElseThrow(() -> new MemberProfileImageException(ErrorStatus.MEMBERPROFILEIMAGE_NOT_FOUND));

        return MemberConverter.toGetMemberPreviewResponseDto(member, latestPublicMemberProfileImage);
    }



    @Transactional(readOnly = true)
    public MemberResponseDto.OtherProfileDto getOtherProfileByMemberId(Long memberId, int page) {
        // 회원 기본 정보 조회
        Member member = memberQueryService.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        // 포스트 수 조회
        int postCount = postQueryService.getPostCount(memberId);

        // 매칭 수 조회
        int matchCount = applicationService.countMatchedApplications(memberId);

        // 프로필 이미지 1개 조회
        MemberProfileImage image = memberProfileImageQueryService.getLatestPublicMemberProfileImage(memberId)
                .orElseThrow(() -> new MemberProfileImageException(ErrorStatus.MEMBERPROFILEIMAGE_NOT_FOUND));

        return MemberConverter.ToOtherProfileDto(member, postCount, matchCount, image);
    }

    @Transactional(readOnly = true)
    public MemberResponseDto.GetMypageMemberProfileResultDto getMyProfileByMemberId(Long memberId) {

        // 회원 기본 정보 조회
        Member member = memberQueryService.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        // 포스트 수 조회
        int postCount = postQueryService.getPostCount(memberId);

        // 매칭 수 조회
        int matchCount = applicationQueryService.countMatchedApplications(memberId);

        // 대표 프로필 이미지 1개 조회
        MemberProfileImage image = memberProfileImageQueryService.getLatestPublicMemberProfileImage(memberId)
                .orElseThrow(() -> new MemberProfileImageException(ErrorStatus.MEMBERPROFILEIMAGE_NOT_FOUND));


        return MemberConverter.toGetMemberProfileResponseDto(member, image, postCount, matchCount);
    }



    @Transactional
    public MemberResponseDto.GetMypageMemberProfileEditResultDto updateMypageMemberProfile(Long memberId, MemberRequestDto.UpdateMemberProfileDto request) {

        // 회원 기본 정보 조회
        Member retrievedMember = memberQueryService.findById(memberId)
                .orElseThrow(()-> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        // 회원 기본 정보 수정
        Member updatedMember = memberCommandService.updateMemberProfile(memberId, request);

        // 프로필 이미지 추가
        MemberProfileImage updatedMemberProfileImage = memberProfileImageCommandService.createMemberProfile(memberId, request.getMemberProfileImageURL());

        // 대표 프로필 이미지 1개 조회
        MemberProfileImage latestPublicMemberProfileImage = memberProfileImageQueryService.getLatestPublicMemberProfileImage(memberId)
                .orElseThrow(()-> new MemberProfileImageException(ErrorStatus.MEMBERPROFILEIMAGE_NOT_FOUND));


        return MemberConverter.toUpdateMemberProfileDto(updatedMember,latestPublicMemberProfileImage);
    }
}