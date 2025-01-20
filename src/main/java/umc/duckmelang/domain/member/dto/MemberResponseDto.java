package umc.duckmelang.domain.member.dto;

import lombok.*;
import umc.duckmelang.domain.application.dto.SentApplicationDto;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;

import java.util.List;

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

        int postCnt;
        int matchCnt;

        List<String> memberProfileImageList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }
}
