package umc.duckmelang.domain.member.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.duckmelang.domain.review.domain.Review;
import umc.duckmelang.domain.application.domain.Application;
import umc.duckmelang.domain.bookmark.domain.Bookmark;
import umc.duckmelang.domain.landmine.domain.Landmine;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.global.common.BaseEntity;

import java.util.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applicationList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarkList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Landmine> landmineList = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> sentReviewList = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> receivedReviewList = new ArrayList<>();
}
