package umc.duckmelang.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class MemberResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignupResultDto {
        Long memberId;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SelectIdolsResultDto{
        private Long memberId;
        private List<Long> idolCategoryIds;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SelectEventsResultDto {
        private Long memberId;
        private List<Long> eventCategoryIds;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateLandmineResultDto {
        private Long memberId;
        private List<String> landmineContents;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateMemberProfileImageResultDto {
        private Long memberId;
        private String memberProfileImageURL;
        private boolean isPublic;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateIntroductionResultDto {
        private Long memberId;
        private String introduction;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetMypageMemberPreviewResultDto {
        private Long memberId;
        private String nickname;
        private boolean gender;
        private int age;
        private String latestPublicMemberProfileImage;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetMypageMemberProfileResultDto {
        private Long memberId;
        private String nickname;
        private boolean gender;
        private int age;
        private String latestPublicMemberProfileImage;
        private String introduction;
        private long postCount;
        private long succeedApplicationCount;
    }



//    //member의 프로필을 수정하는 화면에 진입하면, 기존 프로필 정보들을 보여주는 추가 API가 필요할 듯하다.

//    @Builder
//    @Getter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class GetMypageMemberProfileBeforeEditResultDto {
//        private Long memberId;
//        private String nickname;
//        private String latestPublicMemberProfileImage;
//        private String introduction;
//    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetMypageMemberProfileEditResultDto {
        private Long memberId;
        private String nickname;
        private String latestPublicMemberProfileImage;
        private String introduction;
    }


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
