package umc.duckmelang.domain.auth.inmemory;

import org.springframework.data.annotation.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@RedisHash(value= "RefreshToken",timeToLive = 60 * 60 * 24 * 7)
public class RefreshToken{

    @Id
    private String token;

    private Long memberId;

    @Builder
    public RefreshToken(String token, Long memberId) {
        this.token = token;
        this.memberId = memberId;
    }
}
