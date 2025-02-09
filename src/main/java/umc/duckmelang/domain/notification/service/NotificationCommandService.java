package umc.duckmelang.domain.notification.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.notification.domain.enums.NotificationType;

public interface NotificationCommandService {
    void sendLostData(String lastEventId, Long memberId, String emitterId, SseEmitter emitter);
    void send(Member receiver, NotificationType notificationType, String content);
    void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data);
}
