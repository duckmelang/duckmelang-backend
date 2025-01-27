package umc.duckmelang.domain.memberprofileimage.service;

import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;

import java.util.List;
import java.util.Optional;

public interface MemberProfileImageQueryService {
    Optional<MemberProfileImage> getLatestPublicMemberProfileImage(Long userId);
    List<MemberProfileImage> getAllMemberProfileImageByMemberId(Long userId);
}
