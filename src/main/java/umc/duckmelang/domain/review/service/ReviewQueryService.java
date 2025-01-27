package umc.duckmelang.domain.review.service;

import org.springframework.data.domain.Page;
import umc.duckmelang.domain.application.domain.Application;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.review.domain.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewQueryService {
    List<Review> getReviewList(Long memberId);
    Optional<Application> getReviewInformation(Long myId, Long memberId);
}
