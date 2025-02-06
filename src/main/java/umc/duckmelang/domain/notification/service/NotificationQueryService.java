package umc.duckmelang.domain.notification.service;

import umc.duckmelang.domain.notification.domain.Notification;

import java.util.List;

public interface NotificationQueryService {
    List<Notification> getNotificationList(Long memberId);
}
