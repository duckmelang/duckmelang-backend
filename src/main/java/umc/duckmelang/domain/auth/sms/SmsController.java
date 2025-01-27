package umc.duckmelang.domain.auth.sms;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.duckmelang.global.apipayload.ApiResponse;

@RestController
@RequiredArgsConstructor
public class SmsController {
    private final SmsService smsService;

    @PostMapping("/sms/send")
    public ApiResponse<String> sendSms(@RequestBody SmsRequestDto smsRequestDto){
        smsService.sendSms(smsRequestDto);
        return ApiResponse.onSuccess("문자 전송이 완료되었습니다.");
    }
}