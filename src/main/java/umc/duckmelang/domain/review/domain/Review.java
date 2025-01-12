package umc.duckmelang.domain.review.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.global.common.BaseEntity;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(nullable = false, columnDefinition = "BIT(4) CHECK (score >= 0 AND score <= 5)")
    private Short score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "member_id")
    private Member sender; // 리뷰 보내는 회원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", referencedColumnName = "member_id")
    private Member receiver; // 리뷰 보내는 회원

    public void setSender(Member sender) {
        if (this.sender != null){
            this.sender.getSentReviewList().remove(this);
        }
        this.sender = sender;
        sender.getSentReviewList().add(this);
    }

    public void setReceiver(Member receiver) {
        if (this.receiver != null){
            this.receiver.getReceivedReviewList().remove(this);
        }
        this.receiver = receiver;
        receiver.getReceivedReviewList().add(this);
    }
}
