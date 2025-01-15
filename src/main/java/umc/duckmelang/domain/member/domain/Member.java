package umc.duckmelang.domain.member.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.duckmelang.domain.auth.domain.Auth;
import umc.duckmelang.domain.materelationship.domain.MateRelationship;
import umc.duckmelang.domain.memberidol.domain.MemberIdol;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.review.domain.Review;
import umc.duckmelang.domain.memberevent.domain.MemberEvent;
import umc.duckmelang.domain.application.domain.Application;
import umc.duckmelang.domain.bookmark.domain.Bookmark;
import umc.duckmelang.domain.landmine.domain.Landmine;
import umc.duckmelang.global.common.BaseEntity;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(length = 30)
    private String name;

    @Column(length = 30)
    private String nickname;

    @Column(length = 500)
    private String introduction;

    @Column(nullable = true)
    private LocalDate birth;

    private Boolean gender;

    @Column(columnDefinition = "TINYTEXT")
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberProfileImage> memberProfileImageList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Auth> authList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberIdol> memberIdolList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberEvent> memberEventList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applicationList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarkList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Landmine> landmineList = new ArrayList<>();

    //Review
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> sentReviewList = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> receivedReviewList = new ArrayList<>();

    //mateRelationship
    @OneToMany(mappedBy = "firstMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MateRelationship> mateRelationshipinFirstList = new ArrayList<>();

    @OneToMany(mappedBy = "secondMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MateRelationship> mateRelationshipinSecondList = new ArrayList<>();

    // 비밀번호 설정 함수
    public void encodePassword(String password){
        this.password=password;
    }
}
