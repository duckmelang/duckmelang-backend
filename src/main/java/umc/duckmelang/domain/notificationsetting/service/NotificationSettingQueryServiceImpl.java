package umc.duckmelang.domain.notificationsetting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.duckmelang.domain.notificationsetting.domain.NotificationSetting;
import umc.duckmelang.domain.notificationsetting.repository.NotificationSettingRepository;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.NotificationSettingException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationSettingQueryServiceImpl implements NotificationSettingQueryService {
    private final NotificationSettingRepository notificationSettingRepository;

    @Override
    public NotificationSetting findNotificationSetting(Long memberId){
        return notificationSettingRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotificationSettingException(ErrorStatus.NOTIFICATION_SETTING_NOT_FOUND));

    }
}
