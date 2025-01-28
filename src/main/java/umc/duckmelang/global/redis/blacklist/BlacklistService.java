package umc.duckmelang.global.redis.blacklist;

public interface BlacklistService {
    void addToBlacklist(String token, long expiration);
    boolean isTokenBlacklisted(String token);
}
