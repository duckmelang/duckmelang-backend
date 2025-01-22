package umc.duckmelang.global.redis.blacklist;

import org.springframework.stereotype.Service;

@Service
public interface BlacklistService {
    void addToBlacklist(String token, long expiration);
    boolean isTokenBlacklisted(String token);
    void removeFromBlacklist(String token);
}
