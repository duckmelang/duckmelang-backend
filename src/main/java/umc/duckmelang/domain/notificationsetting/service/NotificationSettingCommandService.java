package umc.duckmelang.domain.notificationsetting.service;

import umc.duckmelang.domain.notificationsetting.domain.NotificationSetting;
import umc.duckmelang.domain.notificationsetting.dto.NotificationSettingRequestDto;

import java.util.Map;

public interface NotificationSettingCommandService {
    void updateNotificationSetting(Long memberId, NotificationSettingRequestDto.UpdateNotificationSettingRequestDto request);
}
