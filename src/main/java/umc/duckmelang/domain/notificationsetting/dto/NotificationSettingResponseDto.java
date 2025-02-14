package umc.duckmelang.domain.notificationsetting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.duckmelang.domain.notification.domain.enums.NotificationType;

import java.time.LocalDateTime;

public class NotificationSettingResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NotificationSettingDto {
        private Long notificationSettingId;
        private Long memberId;
        private Boolean chatNotificationEnabled;
        private Boolean requestNotificationEnabled;
        private Boolean reviewNotificationEnabled;
        private Boolean bookmarkNotificationEnabled;

    }

}
