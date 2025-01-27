package umc.duckmelang.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import umc.duckmelang.domain.application.domain.Application;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.review.domain.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r JOIN FETCH r.receiver WHERE r.receiver.id = :receiverId")
    List<Review> findReceivedReview(Long receiverId);

    @Query("SELECT a FROM Application a " +
            "JOIN FETCH a.post p " +
            "JOIN FETCH p.member pm " +
            "WHERE (pm.id = :memberId AND a.member.id = :myId) " +
            "OR (a.member.id = :memberId AND pm.id = :myId)")
    Optional<Application> findReviewInformation(Long myId, Long memberId);
}
