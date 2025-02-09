package umc.duckmelang.domain.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.notification.converter.NotificationConverter;
import umc.duckmelang.domain.notification.domain.Notification;
import umc.duckmelang.domain.notification.domain.enums.NotificationType;
import umc.duckmelang.domain.notification.dto.NotificationResponseDto;
import umc.duckmelang.domain.notification.repository.EmitterRepository;
import umc.duckmelang.domain.notification.repository.NotificationRepository;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationCommandServiceImpl implements NotificationCommandService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; //1시간 후 자동 종료
    // SSE 연결 지속 시간 설정

    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;

    @Override
    public void sendLostData(String lastEventId, Long memberId, String emitterId, SseEmitter emitter) { // (6)
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(memberId);
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }
    @Override
    public void send(Member receiver, NotificationType notificationType, String content) {
        Notification notification = notificationRepository.save(NotificationConverter.toNotification(receiver, notificationType, content)); // (2-1)
        Long receiverId = receiver.getId(); // (2-2)
        String eventId = receiverId + "_" + System.currentTimeMillis(); // (2-3)
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverId);

        emitters.forEach((key, emitter) -> {
            // NotificationDto 생성
            NotificationResponseDto.NotificationDto notificationDto = NotificationConverter.notificationDto(notification);
            emitterRepository.saveEventCache(key, notification);
            sendNotification(emitter, eventId, key, notificationDto);
        });
    }
    @Override
    public void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) { // (4)
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name("sse")
                    .data(data)
            );
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }
}
