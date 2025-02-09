package umc.duckmelang.domain.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import umc.duckmelang.domain.notification.converter.NotificationConverter;
import umc.duckmelang.domain.notification.domain.Notification;
import umc.duckmelang.domain.notification.dto.NotificationResponseDto;
import umc.duckmelang.domain.notification.service.NotificationQueryService;
import umc.duckmelang.global.annotations.CommonApiResponses;
import umc.duckmelang.global.apipayload.ApiResponse;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Validated
public class NotificationController {
    private final NotificationQueryService notificationQueryService;

    @GetMapping("")
    @CommonApiResponses
    @Operation(summary = "알림 목록 조회 API", description = "지금까지의 모든 알림 내역을 보여줍니다. memberId는 추후 JWT에서 추출 예정")
    public ApiResponse<NotificationResponseDto.NotificationListDto> getNotificationList (@RequestParam Long memberId){
        List<Notification> NotificationList = notificationQueryService.getNotificationList(memberId);
        return ApiResponse.onSuccess(NotificationConverter.notificationListDto(NotificationList));
    }

    @GetMapping(value ="/subscribe", produces = "text/event-stream")
    @CommonApiResponses
    @Operation(summary = "실시간 알림 API", description = "프론트에서의 요청 없이 자동으로 통신합니다.")
    public SseEmitter getRealtimeNotification (@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId, @RequestParam Long memberId) {
        return notificationQueryService.subscribe(memberId, lastEventId);
    }
}
