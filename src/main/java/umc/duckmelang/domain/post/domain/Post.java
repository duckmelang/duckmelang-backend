package umc.duckmelang.domain.post.domain;


import jakarta.persistence.*;
import lombok.*;

import umc.duckmelang.domain.application.domain.Application;
import umc.duckmelang.domain.bookmark.domain.Bookmark;
import umc.duckmelang.domain.eventcategory.domain.EventCategory;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.postidol.domain.PostIdol;
import umc.duckmelang.domain.postimage.domain.PostImage;
import umc.duckmelang.global.common.BaseEntity;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_category_id")
    private EventCategory eventCategory;

    @Column(name = "title", columnDefinition = "TINYTEXT", nullable = false)
    private String title;

    @Column(name = "event_date", columnDefinition = "DATE", nullable = false)
    private LocalDate eventDate;

    @Column(name = "wanted", columnDefinition = "BIT", nullable = false)
    private Short wanted;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostIdol> postIdolList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarkList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> postImageList = new ArrayList<>();

    //n:1 단방향 고려
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applicationList = new ArrayList<>();

    // 연관관계 편의 메서드
    public void setMember(Member member) {
        if (this.member != null) {
            this.member.getPostList().remove(this);
        }
        this.member = member;
        if (member != null) {
            member.getPostList().add(this);
        }
    }

    // 연관관계 편의 메서드
    public void setEventCategory(EventCategory eventCategory) {
        if (this.eventCategory != null) {
            this.eventCategory.getPostList().remove(this);
        }
        this.eventCategory = eventCategory;
        if (eventCategory != null) {
            eventCategory.getPostList().add(this);
        }
    }
}