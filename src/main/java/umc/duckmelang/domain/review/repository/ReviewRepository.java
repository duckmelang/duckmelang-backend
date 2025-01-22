package umc.duckmelang.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import umc.duckmelang.domain.review.domain.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.receiver.id = :receiverId")
    List<Review> findReceivedReview(Long receiverId);
}
