package umc.duckmelang.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.duckmelang.domain.application.domain.Application;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.review.domain.Review;
import umc.duckmelang.domain.review.dto.ReviewResponseDto;
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
    public List<Application> getReviewInformation(Long myId, Long memberId) {
        return reviewRepository.findReviewInformation(myId, memberId);
    }

//    리뷰 평균값 계산
    @Override
    public double calculateAverageScore(List<Review> reviewList){
        if (reviewList == null || reviewList.isEmpty()) {
            return 0.0;
        }
        return Math.round(reviewList.stream()
                .mapToInt(Review::getScore)
                .average()
                .orElse(0) * 10) / 10.0;

    }
}
