package umc.duckmelang.domain.eventcategory.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.global.common.BaseEntity;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventCategory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_category_id")
    private Long id;

    @Column(name = "name", columnDefinition = "TINYTEXT", nullable = false)
    private String name;

    @Column(name = "thumbnail_image_url", columnDefinition = "VARCHAR(1024)")
    private String thumbnailImageUrl;

    @OneToMany(mappedBy = "eventCategory", cascade = CascadeType.ALL)
    private List<Post> postList = new ArrayList<>();
}
