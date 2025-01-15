package umc.duckmelang.domain.memberprofileimage.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.global.common.BaseEntity;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberProfileImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_profile_image_id")
    private Long id;

    @Column(nullable = false, length = 1024)
    private String memberImage; // S3 URL을 저장할 필드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void setMember(Member member) {
        if (this.member != null) {
            member.getMemberProfileImageList().remove(this);
        }
        this.member = member;
        if (member != null)
            member.getMemberProfileImageList().add(this);
    }
}