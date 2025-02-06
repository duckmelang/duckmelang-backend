package umc.duckmelang.domain.post.service;

import org.springframework.web.multipart.MultipartFile;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.dto.PostRequestDto;
import java.util.*;

public interface PostCommandService {
    Post joinPost(PostRequestDto.PostJoinDto request, Long memberId);
    Post joinPost(PostRequestDto.PostJoinDto request, Long memberId, List<MultipartFile> postImages);
    Post patchPostStatus(Long postId);

}
