package umc.duckmelang.domain.auth.sms;

import umc.duckmelang.domain.auth.sms.dto.SmsRequestDto;
import umc.duckmelang.domain.auth.sms.dto.SmsVerifyDto;

public interface SmsService {
    void sendSms(SmsRequestDto smsRequestDto);
    boolean verifyCode(SmsVerifyDto smsVerifyDto);
}
