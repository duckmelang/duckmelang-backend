package umc.duckmelang.domain.refreshtoken.domain;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@RedisHash(value= "refreshToken",timeToLive = 60 * 60 * 24 * 7)
public class RefreshToken{

    @Id
    private String id;
    private Long memberId;
    private String token;

    @Builder
    public RefreshToken(String  id, Long memberId, String token, Long ttl){
        this.id = id;
        this.memberId = memberId;
        this.token = token;
    }
}
