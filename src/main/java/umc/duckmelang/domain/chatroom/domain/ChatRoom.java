package umc.duckmelang.domain.chatroom.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import umc.duckmelang.global.common.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long postId;   // 게시글 id (작성자 쪽 id 조회 가능)

    private Long otherMemberId;   // 게시글을 보는 쪽 회원

    private boolean hasMatched;   // 매칭 성사 여부

    private boolean hasSenderReviewDone;    // 작성자 리뷰 완료 여부

    private boolean hasReceiverReviewDone; // 보는쪽 리뷰 완료 여부

}