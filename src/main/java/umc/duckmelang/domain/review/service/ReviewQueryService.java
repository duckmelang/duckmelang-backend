package umc.duckmelang.domain.review.service;

import org.springframework.data.domain.Page;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.review.domain.Review;

import java.util.List;

public interface ReviewQueryService {
    List<Review> getReviewList(Long memberId);
}
