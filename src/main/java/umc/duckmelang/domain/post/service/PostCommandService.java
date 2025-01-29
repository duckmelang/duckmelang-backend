package umc.duckmelang.domain.post.service;

import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.dto.PostRequestDto;

public interface PostCommandService {
    Post joinPost(PostRequestDto.PostJoinDto request, Long memberId);
    Post patchPostStatus(Long postId);

}
