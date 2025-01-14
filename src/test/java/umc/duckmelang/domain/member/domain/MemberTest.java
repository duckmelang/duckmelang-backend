package umc.duckmelang.domain.member.domain;

import org.junit.jupiter.api.BeforeEach;
import umc.duckmelang.domain.review.domain.Review;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {
    private Member member;
    private Member otherMember;
    private Review review1;
    private Review review2;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .id(1L)
                .sentReviewList(new ArrayList<>())
                .receivedReviewList(new ArrayList<>())
                .build();

        otherMember = Member.builder()
                .id(2L)
                .sentReviewList(new ArrayList<>())
                .receivedReviewList(new ArrayList<>())
                .build();

        review1 = Review.builder()
                .id(1L)
                .score((short) 4)
                .build();

        review2 = Review.builder()
                .id(2L)
                .score((short) 5)
                .build();
    }

    @Nested
    @DisplayName("보낸 리뷰 테스트")
    class SentReviewTests {
        @Test
        @DisplayName("멤버가 보낸 리뷰 목록이 정상적으로 추가되는지 테스트")
        void addSentReviewTest() {
            // When
            review1.setSender(member);
            review2.setSender(member);

            // Then
            assertThat(member.getSentReviewList())
                    .hasSize(2)
                    .contains(review1, review2);
            assertThat(review1.getSender()).isEqualTo(member);
            assertThat(review2.getSender()).isEqualTo(member);
        }

        @Test
        @DisplayName("보낸 리뷰의 sender가 변경되면 리스트에서 제거되는지 테스트")
        void removeSentReviewTest() {
            // Given
            review1.setSender(member);

            // When
            review1.setSender(otherMember);

            // Then
            assertThat(member.getSentReviewList())
                    .hasSize(0)
                    .doesNotContain(review1);
            assertThat(otherMember.getSentReviewList())
                    .hasSize(1)
                    .contains(review1);
        }
    }

    @Nested
    @DisplayName("받은 리뷰 테스트")
    class ReceivedReviewTests {
        @Test
        @DisplayName("멤버가 받은 리뷰 목록이 정상적으로 추가되는지 테스트")
        void addReceivedReviewTest() {
            // When
            review1.setReceiver(member);
            review2.setReceiver(member);

            // Then
            assertThat(member.getReceivedReviewList())
                    .hasSize(2)
                    .contains(review1, review2);
            assertThat(review1.getReceiver()).isEqualTo(member);
            assertThat(review2.getReceiver()).isEqualTo(member);
        }

        @Test
        @DisplayName("받은 리뷰의 receiver가 변경되면 리스트에서 제거되는지 테스트")
        void removeReceivedReviewTest() {
            // Given
            review1.setReceiver(member);

            // When
            review1.setReceiver(otherMember);

            // Then
            assertThat(member.getReceivedReviewList())
                    .hasSize(0)
                    .doesNotContain(review1);
            assertThat(otherMember.getReceivedReviewList())
                    .hasSize(1)
                    .contains(review1);
        }
    }

    @Test
    @DisplayName("동일한 리뷰가 여러 번 추가되지 않는지 테스트")
    void preventDuplicateReviewTest() {
        // When
        review1.setSender(member);
        review1.setSender(member); // 중복 추가 시도

        // Then
        assertThat(member.getSentReviewList())
                .hasSize(1)
                .containsExactly(review1);  // containsOnce를 containsExactly로 수정
    }
}