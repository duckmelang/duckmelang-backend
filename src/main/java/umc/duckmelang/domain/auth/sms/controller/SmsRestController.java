package umc.duckmelang.domain.auth.sms.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import umc.duckmelang.domain.auth.sms.service.SmsService;
import umc.duckmelang.domain.auth.sms.dto.SmsRequestDto;
import umc.duckmelang.domain.auth.sms.dto.SmsVerifyDto;
import umc.duckmelang.global.apipayload.ApiResponse;

@RestController
@RequiredArgsConstructor
public class SmsRestController {
    private final SmsService smsService;

    @Operation(summary = "문자 전송 API", description = "사용자의 휴대폰 번호로 인증번호를 전송하는 API입니다.")
    @PostMapping("/sms/send")
    public ApiResponse<String> sendSms(@RequestBody SmsRequestDto smsRequestDto){
        smsService.sendSms(smsRequestDto);
        return ApiResponse.onSuccess("문자 전송이 완료되었습니다.");
    }

    @Operation(summary = "문자 인증 API", description = "사용자가 입력한 인증번호를 검증하는 API입니다.")
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