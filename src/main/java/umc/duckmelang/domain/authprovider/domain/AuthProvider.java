package umc.duckmelang.domain.authprovider.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.duckmelang.domain.authprovider.enums.ProviderKind;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.global.common.BaseEntity;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AuthProvider extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_provider_id")
    private Long id;

    @Column(unique = true)
    private String textId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(8)")
    private ProviderKind provider;

    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void setMember(Member member) {
        if (this.member != null) {
            this.member.getAuthProviderList().remove(this);
        }
        this.member = member;
        if (member != null) {
            member.getAuthProviderList().add(this);
        }
    }
}
