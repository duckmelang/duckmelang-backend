package umc.duckmelang.domain.memberprofileimage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.repository.MemberProfileImageRepository;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.handler.MemberProfileImageException;

@Service
@RequiredArgsConstructor
public class MemberProfileImageQueryServiceImpl implements MemberProfileImageQueryService {
    private final MemberProfileImageRepository memberProfileImageRepository;

    @Override
    public MemberProfileImage findLatestOneByMemberId(Long id) {

        return memberProfileImageRepository.findFirstByMemberIdOrderByCreatedAtDesc(id)
                .orElseThrow(()->new MemberProfileImageException(ErrorStatus.MEMBER_PROFILE_IMAGE_NOT_FOUND.getCode()));
    }

    @Override
    public Page<MemberProfileImage> getProfileImagesByMemberId(Long memberId, int page) {
        return memberProfileImageRepository.findAllByMemberId(memberId, PageRequest.of(page,10));
    }
}
