package umc.duckmelang.domain.application.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.duckmelang.domain.application.domain.enums.ApplicationStatus;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.global.common.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Application extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
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
        if (this.post != null) {
            this.post.getApplicationList().remove(this);
        }
        this.post = post;
        if (post != null) {
            post.getApplicationList().add(this);
        }
    }

    public void setMember(Member member) {
        if (this.member != null) {
            this.member.getApplicationList().remove(this);
        }
        this.member = member;
        if (member != null) {
            member.getApplicationList().add(this);
        }
    }
}