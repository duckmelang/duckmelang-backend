package umc.duckmelang.domain.refreshtoken.repository;

import org.springframework.data.repository.CrudRepository;
import umc.duckmelang.domain.refreshtoken.domain.RefreshToken;


public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
