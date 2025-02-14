package umc.duckmelang.domain.notificationsetting.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.duckmelang.domain.notificationsetting.converter.NotificationSettingConverter;
import umc.duckmelang.domain.notificationsetting.domain.NotificationSetting;
import umc.duckmelang.domain.notificationsetting.dto.NotificationSettingResponseDto;
import umc.duckmelang.domain.notificationsetting.service.NotificationSettingQueryService;
import umc.duckmelang.global.annotations.CommonApiResponses;
import umc.duckmelang.global.apipayload.ApiResponse;
import umc.duckmelang.global.security.user.CustomUserDetails;

@RestController
@RequestMapping("/notifications/setting")
@RequiredArgsConstructor
public class NotificationSettingRestController {
    private final NotificationSettingQueryService notificationSettingQueryService;

    @Operation(summary = "알림 설정 조회 API", description = "해당하는 멤버의 알림 설정을 조회합니다.")
    @GetMapping("")
    @CommonApiResponses
    public ApiResponse<NotificationSettingResponseDto.NotificationSettingDto> getNotificationSetting(@AuthenticationPrincipal CustomUserDetails userDetails){
        Long memberId = userDetails.getMemberId();
        NotificationSetting notificationSetting = notificationSettingQueryService.findNotificationSetting(memberId);
        return ApiResponse.onSuccess(NotificationSettingConverter.notificationSettingDto(notificationSetting));

    }
}
