package umc.duckmelang.domain.review.domain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import umc.duckmelang.domain.member.domain.Member;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

class ReviewTest {
    private Member sender;
    private Member receiver;
    private Review review;

    @BeforeEach
    void setUp() {
        // Given
        sender = Member.builder()
                .id(1L)
                .sentReviewList(new ArrayList<>())
                .receivedReviewList(new ArrayList<>())
                .build();

        receiver = Member.builder()
                .id(2L)
                .sentReviewList(new ArrayList<>())
                .receivedReviewList(new ArrayList<>())
                .build();

        review = Review.builder()
                .id(1L)
                .score((short) 4)
                .build();
    }

    @Test
    @DisplayName("리뷰 생성 시 sender와 receiver가 정상적으로 설정되어야 한다")
    void createReviewWithSenderAndReceiver() {
        // When
        review.setSender(sender);
        review.setReceiver(receiver);

        // Then
        assertThat(review.getSender()).isEqualTo(sender);
        assertThat(review.getReceiver()).isEqualTo(receiver);
        assertThat(sender.getSentReviewList()).contains(review);
        assertThat(receiver.getReceivedReviewList()).contains(review);
    }

    @Test
    @DisplayName("리뷰의 sender를 변경하면 이전 sender의 sentReviewList에서 제거되어야 한다")
    void changeSenderShouldUpdateLists() {
        // Given
        review.setSender(sender);
        Member newSender = Member.builder()
                .id(3L)
                .sentReviewList(new ArrayList<>())
                .receivedReviewList(new ArrayList<>())
                .build();

        // When
        review.setSender(newSender);

        // Then
        assertThat(sender.getSentReviewList()).doesNotContain(review);
        assertThat(newSender.getSentReviewList()).contains(review);
        assertThat(review.getSender()).isEqualTo(newSender);
    }

    @Test
    @DisplayName("리뷰의 receiver를 변경하면 이전 receiver의 receivedReviewList에서 제거되어야 한다")
    void changeReceiverShouldUpdateLists() {
        // Given
        review.setReceiver(receiver);
        Member newReceiver = Member.builder()
                .id(4L)
                .sentReviewList(new ArrayList<>())
                .receivedReviewList(new ArrayList<>())
                .build();

        // When
        review.setReceiver(newReceiver);

        // Then
        assertThat(receiver.getReceivedReviewList()).doesNotContain(review);
        assertThat(newReceiver.getReceivedReviewList()).contains(review);
        assertThat(review.getReceiver()).isEqualTo(newReceiver);
    }

    @Test
    @DisplayName("리뷰 점수는 0에서 5 사이의 값이어야 한다")
    void reviewScoreShouldBeInValidRange() {
        // Given & When
        Review validReview = Review.builder()
                .id(2L)
                .score((short) 5)
                .build();

        // Then
        assertThat(validReview.getScore()).isBetween((short) 0, (short) 5);
    }
}