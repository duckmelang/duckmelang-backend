package umc.duckmelang.domain.auth.sms;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import umc.duckmelang.domain.auth.sms.dto.SmsRequestDto;
import umc.duckmelang.domain.auth.sms.dto.SmsVerifyDto;
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

    @PostMapping("/sms/verify")
    public ApiResponse<String> verifyCode(@RequestBody SmsVerifyDto smsVerifyDto){
        boolean verify = smsService.verifyCode(smsVerifyDto);
        if(verify){
            return ApiResponse.onSuccess("인증이 되었습니다.");
        } else{
            return ApiResponse.onFailure("400", "인증번호가 일치하지 않거나 만료되었습니다.", null);
        }
    }
}