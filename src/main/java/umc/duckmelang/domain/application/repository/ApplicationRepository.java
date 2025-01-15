package umc.duckmelang.domain.application.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import umc.duckmelang.domain.application.domain.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    @EntityGraph(attributePaths = {"member","post", "post.member"})
    Optional<Application> findByIdAndPostMemberId(Long id, Long memberId);

    Optional<Application> findByIdAndMemberId(Long id, Long memberId);
}
