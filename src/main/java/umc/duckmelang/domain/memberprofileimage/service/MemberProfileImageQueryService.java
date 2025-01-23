package umc.duckmelang.domain.memberprofileimage.service;

import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;

import java.util.Optional;

public interface MemberProfileImageQueryService {
    public Optional<MemberProfileImage> getLatestPublicMemberProfileImage(Long userId);
}
