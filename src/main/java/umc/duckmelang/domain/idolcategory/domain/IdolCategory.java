package umc.duckmelang.domain.idolcategory.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.duckmelang.domain.memberidol.domain.MemberIdol;
import umc.duckmelang.domain.postidol.domain.PostIdol;
import umc.duckmelang.global.common.BaseEntity;


import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class IdolCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(length = 30)
    private String company;

    @Column(length = 1024)
    private String profileImage;

    @OneToMany(mappedBy = "idolCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostIdol> postIdols = new ArrayList<>();

    @OneToMany(mappedBy = "idolCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberIdol> memberIdols = new ArrayList<>();

}
