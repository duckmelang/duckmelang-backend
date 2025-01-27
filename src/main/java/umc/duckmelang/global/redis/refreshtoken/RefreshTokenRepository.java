package umc.duckmelang.global.redis.refreshtoken;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByMemberId(Long memberId);
}
