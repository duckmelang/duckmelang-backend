package umc.duckmelang.domain.memberprofileimage.dto;

import lombok.Builder;
import lombok.Getter;

public class MemberProfileImageRequestDto {

    @Builder
    @Getter
    public static class DeleteMemberProfileImageDto {
        private Long ImageId;
    }
}
