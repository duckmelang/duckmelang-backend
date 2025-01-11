package umc.duckmelang.domain.postimage.entity;

import jakarta.persistence.*;
import lombok.*;
import umc.duckmelang.domain.post.entity.Post;
import umc.duckmelang.global.common.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_image_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "post_image_url", columnDefinition = "VARCHAR(1024)")
    private String postImageUrl;

    // 연관관계 편의 메서드
    public void setPost(Post post) {
        // 기존 관계 제거
        if (this.post != null) {
            this.post.getPostImageList().remove(this);
        }
        this.post = post;
        // 새로운 관계 설정
        if (post != null) {
            post.getPostImageList().add(this);
        }
    }
}