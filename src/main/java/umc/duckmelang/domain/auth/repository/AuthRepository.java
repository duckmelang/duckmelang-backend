package umc.duckmelang.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.duckmelang.domain.auth.domain.Auth;
import umc.duckmelang.domain.auth.domain.enums.ProviderKind;
import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findByTextIdAndProvider(String textId, ProviderKind provider);
}
