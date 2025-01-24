package umc.duckmelang.domain.memberprofileimage.service;

import jakarta.validation.Valid;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.dto.MemberProfileImageRequestDto;

public interface MemberProfileImageCommandService {
    public void deleteProfileImage(Long memberId, MemberProfileImageRequestDto.MemberProfileImageDto request);
    public MemberProfileImage updateProfileImageStatus(Long memberId, MemberProfileImageRequestDto.MemberProfileImageDto request);
    public MemberProfileImage createMemberProfile(Long memberId, String memberProfileImageURL);
}
