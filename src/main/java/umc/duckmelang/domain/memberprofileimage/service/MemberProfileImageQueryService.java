package umc.duckmelang.domain.memberprofileimage.service;

import org.springframework.data.domain.Page;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;

import java.util.List;
import java.util.Optional;

public interface MemberProfileImageQueryService {
    Optional<MemberProfileImage> getLatestPublicMemberProfileImage(Long userId);
    Page<MemberProfileImage> getAllMemberProfileImageByMemberId(Long userId, int page);
    Page<MemberProfileImage> getPublicMemberProfileImageByMemberId(Long userId, int page);
}
