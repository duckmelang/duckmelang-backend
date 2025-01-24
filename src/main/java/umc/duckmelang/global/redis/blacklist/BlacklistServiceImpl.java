package umc.duckmelang.global.redis.blacklist;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BlacklistServiceImpl implements BlacklistService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String BLACKLIST_PREFIX = "blacklist:token:";

    // 블랙리스트에 토큰을 추가
    @Override
    public void addToBlacklist(String token, long expiration) {
        String key = BLACKLIST_PREFIX + token;
        redisTemplate.opsForValue().set(key, "logout", expiration, TimeUnit.MILLISECONDS);
    }

    // 블랙리스트에서 토큰 존재 여부 확인
    @Override
    public boolean isTokenBlacklisted(String token) {
        String key = BLACKLIST_PREFIX + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}