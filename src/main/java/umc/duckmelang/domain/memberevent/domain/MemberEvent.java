package umc.duckmelang.domain.memberevent.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.duckmelang.domain.eventcategory.domain.EventCategory;
import umc.duckmelang.global.common.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberEvent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //n:1 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_category_id")
    private EventCategory eventCategory;
}
