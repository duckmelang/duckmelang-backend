package umc.duckmelang.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.duckmelang.domain.auth.domain.Auth;

public interface AuthRepository extends JpaRepository<Auth, Long> {
}
