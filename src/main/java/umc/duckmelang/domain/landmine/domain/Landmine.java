package umc.duckmelang.domain.landmine.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.global.common.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Landmine extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "landmine_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    public void setMember(Member member) {
        if (this.member != null) {
            this.member.getLandmineList().remove(this);
        }

        this.member = member;

        member.getLandmineList().add(this);
    }
}
