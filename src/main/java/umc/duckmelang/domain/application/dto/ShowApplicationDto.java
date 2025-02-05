package umc.duckmelang.domain.application.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.duckmelang.domain.application.domain.enums.ApplicationStatus;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShowApplicationDto {
    Long postId;
    String postTitle;
    String postImage;
    String oppositeNickname;
    String oppositeProfileImage;
    Long applicationId;
    LocalDateTime applicationCreatedAt;
    ApplicationStatus applicationStatus;
}
