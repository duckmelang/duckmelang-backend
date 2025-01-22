package umc.duckmelang.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.duckmelang.domain.review.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
