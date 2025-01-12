package umc.duckmelang.domain.postidol.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.global.common.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostIdol extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id")
    private Post post;

    // 연관관계 편의 메서드
    public void setPost(Post post) {
        // 기존 관계 제거
        if (this.post != null) {
            this.post.getPostIdolList().remove(this);
        }
        this.post = post;
        // 새로운 관계 설정
        if (post != null) {
            post.getPostIdolList().add(this);
        }
    }
}
