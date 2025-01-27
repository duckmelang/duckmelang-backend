package umc.duckmelang.domain.member.dto;

import lombok.*;
import umc.duckmelang.domain.postimage.dto.PostImageResponseDto;

public class MemberResponseDto {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OtherProfileDto {
        String nickname;
        String gender;
        int age;
        String introduction;
        String profileImageUrl;

        int postCnt;
        int matchCnt;
    }
}
