package umc.duckmelang.domain.post.service;

import org.springframework.data.domain.Page;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.postidol.domain.PostIdol;

public interface PostQueryService {
    Page<Post> getPostList(Integer page);
    Page<Post> getPostListByIdol(Long idolId, Integer page);
}
