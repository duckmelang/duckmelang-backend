package umc.duckmelang.domain.memberprofileimage.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.repository.MemberRepository;
import umc.duckmelang.domain.memberprofileimage.converter.MemberProfileImageConverter;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.dto.MemberProfileImageRequestDto;
import umc.duckmelang.domain.memberprofileimage.repository.MemberProfileImageRepository;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.MemberException;
import umc.duckmelang.global.apipayload.exception.MemberProfileImageException;

@Service
@RequiredArgsConstructor
public class MemberProfileImageCommandServiceImpl implements MemberProfileImageCommandService {

    private final MemberProfileImageRepository memberProfileImageRepository;
    private final MemberRepository memberRepository;

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
        MemberProfileImage updatedProfileImage = MemberProfileImageConverter.toMemberProfileImageWithChangedStatus(request.getImageId(), request.isPublic());

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


}
