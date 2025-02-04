package umc.duckmelang.domain.memberprofileimage.service;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.dto.MemberProfileImageRequestDto;

public interface MemberProfileImageCommandService {
    void deleteProfileImage(Long memberId, MemberProfileImageRequestDto.MemberProfileImageDto request);
    MemberProfileImage updateProfileImageStatus(Long memberId, MemberProfileImageRequestDto.MemberProfileImageDto request);
    MemberProfileImage createMemberProfile(Long memberId, String memberProfileImageURL);

    MemberProfileImage createProfileImage(Long memberId, MultipartFile profileImage);
}
