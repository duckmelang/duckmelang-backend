package umc.duckmelang.domain.notification.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import umc.duckmelang.domain.notification.domain.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationQueryService {
    List<Notification> getNotificationList(Long memberId);
    SseEmitter subscribe(Long memberId, String lastEventId);
    String makeTimeIncludeId(Long memberId);
    boolean hasLostData(String lastEventId);
    boolean existsById(Long notificationId);
}
