package umc.duckmelang.domain.memberprofileimage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.repository.MemberProfileImageRepository;
import java.util.List;
@Service
@RequiredArgsConstructor
public class MemberProfileImageQueryServiceImpl implements MemberProfileImageQueryService {
    private final MemberProfileImageRepository memberProfileImageRepository;
    public Page<String> getMemberImages(Long memberId, int page) {
        return memberProfileImageRepository.findImageUrlsByMemberId(memberId, PageRequest.of(page,10));
    }
}
