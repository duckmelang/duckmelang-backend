package umc.duckmelang.domain.memberprofileimage.service;

import org.springframework.web.multipart.MultipartFile;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.dto.MemberProfileImageRequestDto;

public interface MemberProfileImageCommandService {
    void deleteProfileImage(Long memberId, Long imageId);
    MemberProfileImage updateProfileImageStatus(Long memberId, Long imageId, MemberProfileImageRequestDto.UpdateProfileImageStatusDto request);
    MemberProfileImage createProfileImage(Long memberId, MultipartFile profileImage);
}
