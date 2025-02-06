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
    public static class ProfileDto {
        private Long memberId;
        private String nickname;
        private Gender gender;
        private int age;
        private String latestPublicMemberProfileImage;
        private String introduction;
        private long postCount;
        private long matchCount;
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
}
