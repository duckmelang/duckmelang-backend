package umc.duckmelang.domain.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SentApplicationDto {
    Long postId;
    String postTitle;
    String postMemberNickname;
    LocalDate eventDate;
    String eventCategory;
    Long applicationId;

    public SentApplicationDto(Long postId, String postTitle, String nickname,
                              LocalDateTime dateTime, String name, Long applicationId) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postMemberNickname = nickname;
        this.eventDate = dateTime.toLocalDate();  // 여기서 변환
        this.eventCategory = name;
        this.applicationId = applicationId;
    }

    @Override
    public String toString() {
        return postTitle + " " + postMemberNickname + " " + eventDate + " " + eventCategory;
    }
}
