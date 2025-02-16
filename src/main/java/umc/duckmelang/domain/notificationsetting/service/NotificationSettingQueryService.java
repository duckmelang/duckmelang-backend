package umc.duckmelang.domain.notificationsetting.service;

import umc.duckmelang.domain.notificationsetting.domain.NotificationSetting;

public interface NotificationSettingQueryService {
    NotificationSetting findNotificationSetting(Long memberId);
}
