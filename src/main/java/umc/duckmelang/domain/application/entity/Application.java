package umc.duckmelang.domain.application.entity;

import jakarta.persistence.*;
import lombok.*;
import umc.duckmelang.domain.application.enums.ApplicationStatus;
import umc.duckmelang.domain.post.entity.Post;
import umc.duckmelang.global.common.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(8) DEFAULT 'PENDING'")
    private ApplicationStatus status;

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
            this.post.getApplicationList().remove(this);
        }
        this.post = post;
        // 새로운 관계 설정
        if (post != null) {
            post.getApplicationList().add(this);
        }
    }

    public void setMember(Member member) {
        // 기존 관계 제거
        if (this.member != null) {
            this.member.getApplicationList().remove(this);
        }
        this.member = post;
        // 새로운 관계 설정
        if (member != null) {
            member.getApplicationList().add(this);
        }
    }
}