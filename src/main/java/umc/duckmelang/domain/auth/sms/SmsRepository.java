package umc.duckmelang.domain.auth.sms;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class SmsRepository {
    private final String PREFIX = "sms:";
    private final StringRedisTemplate stringRedisTemplate;

    public void createSmsCertification(String phoneNum, String code){
        int LIMIT_TIME = 3 * 60; // 인증 코드의 유효시간
        stringRedisTemplate.opsForValue().set(PREFIX+ phoneNum, code, Duration.ofSeconds(LIMIT_TIME));
    }

    public String getSmsCertification(String phoneNum){
        return stringRedisTemplate.opsForValue().get(PREFIX+phoneNum);
    }

    public void deleteSmsCertification(String phoneNum){
        stringRedisTemplate.delete(PREFIX + phoneNum);
    }

    public boolean hasKey(String phoneNum){
        Boolean hasKey = stringRedisTemplate.hasKey(PREFIX + phoneNum);
        return hasKey != null && hasKey;
    }
}
