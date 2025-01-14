package umc.duckmelang.domain.post.service;

import org.springframework.data.domain.Page;
import umc.duckmelang.domain.post.domain.Post;

public interface PostQueryService {
    Page<Post> getPostList(Integer page);
}
