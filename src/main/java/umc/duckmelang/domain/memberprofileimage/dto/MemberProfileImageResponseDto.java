package umc.duckmelang.domain.memberprofileimage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class MemberProfileImageResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetAllProfileImageResultDto{
        private Long memberId;
        private List<String> profileImageUrls;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteProfileImageResultDto {
        private String succeedMessage;
    }
}
