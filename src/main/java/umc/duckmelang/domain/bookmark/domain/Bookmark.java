package umc.duckmelang.domain.bookmark.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.global.common.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bookmark extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 연관관계 편의 메서드
    public void setPost(Post post) {
        // 기존 관계 제거
        if (this.post != null) {
            this.post.getBookmarkList().remove(this);
        }
        this.post = post;
        // 새로운 관계 설정
        if (post != null) {
            post.getBookmarkList().add(this);
        }
    }

    public void setMember(Member member) {
        // 기존 관계 제거
        if (this.member != null) {
            this.member.getBookmarkList().remove(this);
        }
        this.member = member;
        // 새로운 관계 설정
        if (member != null) {
            member.getBookmarkList().add(this);
        }
    }
}
