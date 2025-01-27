package umc.duckmelang.domain.auth.sms;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SmsServiceImpl implements SmsService{
    private final SmsCertificationUtil smsCertificationUtil;

    @Override
    public void sendSms(SmsRequestDto smsRequestDto){
        String phoneNum = smsRequestDto.getPhoneNum();

        // 6자리 인증 번호 랜덤 생성
        String certificationCode = Integer.toString((int) (Math.random() * (999999 - 100000 + 1)) + 100000);
        smsCertificationUtil.sendSMS(smsRequestDto.getPhoneNum(), certificationCode);
    }
}