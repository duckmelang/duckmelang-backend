package umc.duckmelang.domain.auth.sms;

public interface SmsService {
    void sendSms(SmsRequestDto smsRequestDto);
}
