package umc.duckmelang.domain.auth.sms;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.auth.sms.dto.SmsRequestDto;
import umc.duckmelang.domain.auth.sms.dto.SmsVerifyDto;

@RequiredArgsConstructor
@Service
public class SmsServiceImpl implements SmsService{
    private final SmsCertificationUtil smsCertificationUtil;
    private final SmsRepository smsRepository;

    @Override
    public void sendSms(SmsRequestDto smsRequestDto){
        String phoneNum = smsRequestDto.getPhoneNum();

        // 6자리 인증 번호 랜덤 생성
        String certificationCode = Integer.toString((int) (Math.random() * (999999 - 100000 + 1)) + 100000);
        smsCertificationUtil.sendSMS(smsRequestDto.getPhoneNum(), certificationCode);
        smsRepository.createSmsCertification(phoneNum, certificationCode); // redis에 인증 번호 저장 3분 유효
    }

    @Override
    public boolean verifyCode(SmsVerifyDto smsVerifyDto){
        String phoneNum = smsVerifyDto.getPhoneNum();
        String certificationCode = smsVerifyDto.getCertificationCode();

        boolean isValid = smsRepository.hasKey(phoneNum) && certificationCode.equals(smsRepository.getSmsCertification(phoneNum));
        if(isValid){
            smsRepository.deleteSmsCertification(phoneNum); // 인증 성공하면 redis에서 삭제
        }
        return isValid;
    }
}