package umc.duckmelang.domain.post.service;

import org.springframework.data.domain.Page;
import umc.duckmelang.domain.post.domain.Post;

import java.util.Optional;

public interface PostQueryService {
    Page<Post> getPostList(Integer page);
    Page<Post> getPostListByIdol(Long idolId, Integer page);
    Page<Post> getPostListByMember(Long memberId, Integer page);
    Optional<Post> getPostDetail(Long postId);
    Page<Post> getPostListByTitle(String searchKeyword, Integer page );
    Optional<Post> findById(Long postId);
    Page<Post> getMyPostList(Long memberId, Integer page);
    int getPostCount(Long memberId);
}
