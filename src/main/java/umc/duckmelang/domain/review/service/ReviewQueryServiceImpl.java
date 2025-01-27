package umc.duckmelang.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.duckmelang.domain.application.domain.Application;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.review.domain.Review;
import umc.duckmelang.domain.review.repository.ReviewRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewQueryServiceImpl implements ReviewQueryService {
    private final ReviewRepository reviewRepository;

    @Override
    public List<Review> getReviewList(Long memberId) {
        return reviewRepository.findReceivedReview(memberId);
    }

    @Override
    public Optional<Application> getReviewInformation(Long myId, Long memberId) {
        return reviewRepository.findReviewInformation(myId, memberId);
    }

}
