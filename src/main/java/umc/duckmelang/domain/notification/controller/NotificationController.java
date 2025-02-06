package umc.duckmelang.domain.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import umc.duckmelang.domain.notification.converter.NotificationConverter;
import umc.duckmelang.domain.notification.domain.Notification;
import umc.duckmelang.domain.notification.dto.NotificationResponseDto;
import umc.duckmelang.domain.notification.service.NotificationQueryService;
import umc.duckmelang.global.annotations.CommonApiResponses;
import umc.duckmelang.global.apipayload.ApiResponse;

import java.util.List;

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
}
