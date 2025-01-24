package umc.duckmelang.domain.memberprofileimage.service;

import umc.duckmelang.domain.memberprofileimage.dto.MemberProfileImageRequestDto;

public interface MemberProfileImageCommandService {
    public void deleteProfileImage(Long memberId, MemberProfileImageRequestDto.DeleteMemberProfileImageDto request);
}
