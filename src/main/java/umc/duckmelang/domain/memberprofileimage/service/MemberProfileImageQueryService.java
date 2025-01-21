package umc.duckmelang.domain.memberprofileimage.service;

import org.springframework.data.domain.Page;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;

public interface MemberProfileImageQueryService {
    MemberProfileImage findLatestOneByMemberId(Long id);
    Page<MemberProfileImage> getProfileImagesByMemberId(Long memberId, int page);
}
