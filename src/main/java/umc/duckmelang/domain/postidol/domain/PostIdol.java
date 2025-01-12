package umc.duckmelang.domain.postidol.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.duckmelang.domain.post.domain.Post;

@Entity
@Getter
@AllArgsConstructor
public class PostIdol extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "post_id")
    private Post post;

}
