package umc.duckmelang.domain.uuid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.duckmelang.domain.postimage.domain.PostImage;

public interface UuidRepository extends JpaRepository<PostImage, Long> {
}
