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
public class ReceivedApplicationDto{
    String applicationMemberNickname;
    Long postId;
    String postTitle;
    Long applicationId;
    LocalDateTime applicationCreatedAt;
    ApplicationStatus applicationStatus;
}
