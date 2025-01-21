package umc.duckmelang.domain.memberprofileimage.service;

import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;

public interface MemberProfileImageQueryService {
    MemberProfileImage findLatestOneByMemberId(Long id);
}
