package umc.duckmelang.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.notification.domain.enums.NotificationType;
import umc.duckmelang.domain.review.dto.ReviewResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public class NotificationResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NotificationDto {
        private Long id;
        private String content;
        private Boolean isRead;
        private Boolean isDelivered;
        private NotificationType type;
        private String senderNickname;
        private String extraData;
        private LocalDateTime createdAt;

    }
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NotificationListDto {
        List<NotificationResponseDto.NotificationDto> notificationList;

    }
}
