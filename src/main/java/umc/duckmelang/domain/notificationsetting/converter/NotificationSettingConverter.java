package umc.duckmelang.domain.notificationsetting.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import umc.duckmelang.domain.notificationsetting.domain.NotificationSetting;
import umc.duckmelang.domain.notificationsetting.dto.NotificationSettingResponseDto;

@Component
@RequiredArgsConstructor
public class NotificationSettingConverter {
    public static NotificationSettingResponseDto.NotificationSettingDto notificationSettingDto(NotificationSetting notificationSetting) {
        return NotificationSettingResponseDto.NotificationSettingDto.builder()
                .notificationSettingId(notificationSetting.getId())
                .memberId(notificationSetting.getMember().getId())
                .chatNotificationEnabled(notificationSetting.getChatNotificationEnabled())
                .requestNotificationEnabled(notificationSetting.getRequestNotificationEnabled())
                .reviewNotificationEnabled(notificationSetting.getReviewNotificationEnabled())
                .bookmarkNotificationEnabled(notificationSetting.getBookmarkNotificationEnabled())
                .build();
    }
}
