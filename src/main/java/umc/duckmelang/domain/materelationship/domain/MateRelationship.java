package umc.duckmelang.domain.materelationship.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.application.domain.Application;
import umc.duckmelang.global.common.BaseEntity;



@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MateRelationship extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "first_member_id", nullable = false)
    private Member firstMember; // 첫 번째 회원

    @ManyToOne
    @JoinColumn(name = "second_member_id", nullable = false)
    private Member secondMember; // 두 번째 회원

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id")
    private Application application; // 관련 신청 ID (다른 테이블과 연관 가능)
}

