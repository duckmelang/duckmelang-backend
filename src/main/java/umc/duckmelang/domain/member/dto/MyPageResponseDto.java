package umc.duckmelang.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.duckmelang.domain.member.domain.enums.Gender;

public class MyPageResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MypageMemberPreviewResultDto {
        private Long memberId;
        private String nickname;
        private Gender gender;
        private int age;
        private String latestPublicMemberProfileImage;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MypageMemberProfileResultDto {
        private Long memberId;
        private String nickname;
        private Gender gender;
        private int age;
        private String latestPublicMemberProfileImage;
        private String introduction;
        private long postCount;
        private long succeedApplicationCount;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPageMemberProfileEditBeforeDto {
        private String nickname;
        private String latestPublicMemberProfileImage;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MypageMemberProfileEditResultDto {
        private Long memberId;
        private String nickname;
        private String introduction;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OtherProfileDto {
        String nickname;
        Gender gender;
        int age;
        String introduction;
        String profileImageUrl;

        int postCnt;
        int matchCnt;
    }
}
