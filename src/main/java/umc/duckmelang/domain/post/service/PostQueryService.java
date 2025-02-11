package umc.duckmelang.domain.post.service;

import org.springframework.data.domain.Page;
import umc.duckmelang.domain.member.domain.enums.Gender;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.dto.PostResponseDto;

import java.util.Optional;

public interface PostQueryService {
    PostResponseDto.PostPreviewListDto getFilteredPostList(Integer page, Gender gender, Integer minAge, Integer maxAge);
    Page<Post> getPostListByIdol(Long idolId, Integer page);
    Page<Post> getPostListByMember(Long memberId, Integer page);
    Optional<Post> getPostDetail(Long postId);
    Page<Post> getFilteredPostListByTitle(String keyword, Gender gender, Integer minAge, Integer maxAge, Integer page);
    Optional<Post> findById(Long postId);
    Page<Post> getMyPostList(Long memberId, Integer page);
    int getPostCount(Long memberId);
}
