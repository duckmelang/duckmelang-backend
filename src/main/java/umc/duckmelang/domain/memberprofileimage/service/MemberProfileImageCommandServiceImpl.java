package umc.duckmelang.domain.memberprofileimage.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.repository.MemberRepository;
import umc.duckmelang.domain.memberprofileimage.converter.MemberProfileImageConverter;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.dto.MemberProfileImageRequestDto;
import umc.duckmelang.domain.memberprofileimage.repository.MemberProfileImageRepository;
import umc.duckmelang.domain.uuid.domain.Uuid;
import umc.duckmelang.domain.uuid.repository.UuidRepository;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.MemberException;
import umc.duckmelang.global.apipayload.exception.MemberProfileImageException;
import umc.duckmelang.global.aws.AmazonS3Manager;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberProfileImageCommandServiceImpl implements MemberProfileImageCommandService {

    private final MemberProfileImageRepository memberProfileImageRepository;
    private final MemberRepository memberRepository;
    private final UuidRepository uuidRepository;

    private final AmazonS3Manager s3Manager;

    @Value("${spring.custom.default.profile-image}")
    private String defaultProfileImage;

    @Override
    @Transactional
    public void deleteProfileImage(Long memberId, MemberProfileImageRequestDto.MemberProfileImageDto request) {
        // 회원 조회 및 유효성 검증
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        // 프로필 이미지 조회 및 유효성 검증
        MemberProfileImage profileImage = memberProfileImageRepository.findById(request.getImageId())
                .orElseThrow(() -> new MemberProfileImageException(ErrorStatus.MEMBERPROFILEIMAGE_NOT_FOUND));

        memberProfileImageRepository.delete(profileImage);
    }

    @Override
    @Transactional
    public MemberProfileImage updateProfileImageStatus(Long memberId, MemberProfileImageRequestDto.MemberProfileImageDto request) {
        // 회원 조회 및 유효성 검증
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        // 프로필 이미지 조회 및 유효성 검증
        MemberProfileImage profileImage = memberProfileImageRepository.findById(request.getImageId())
                .orElseThrow(() -> new MemberProfileImageException(ErrorStatus.MEMBERPROFILEIMAGE_NOT_FOUND));

        //프로필 이미지 공개 범위 변경
        MemberProfileImage updatedProfileImage = MemberProfileImageConverter.toMemberProfileImageWithChangedStatus(profileImage, request.isPublicStatus());

        return memberProfileImageRepository.save(updatedProfileImage);
    }

    @Override
    @Transactional
    public MemberProfileImage createMemberProfile(Long memberId, String memberProfileImageURL) {
        // 회원 조회 및 유효성 검증
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        // 프로필 이미지 생성
        MemberProfileImage createdProfileImage = MemberProfileImageConverter.toCreateMemberProfileImage(member, memberProfileImageURL);
        return memberProfileImageRepository.save(createdProfileImage);
    }

    @Override
    public MemberProfileImage createProfileImage(Long memberId, MultipartFile profileImage) {
        // 회원 조회 및 유효성 검증
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        String uuid = UUID.randomUUID().toString();
        Uuid savedUuid = uuidRepository.save(Uuid.builder()
                .uuid(uuid).build());

        // 프로필 사진을 선택하지 않은 경우, 기본 프로필 사진으로 설정
        String profileImageUrl;
        if (profileImage == null || profileImage.isEmpty())
            profileImageUrl = defaultProfileImage;
        else profileImageUrl = s3Manager.uploadFile(s3Manager.generateMemberProfileImageKeyName(savedUuid), profileImage);

        return memberProfileImageRepository.save(MemberProfileImageConverter.toCreateMemberProfileImage(member, profileImageUrl));

    }


}
