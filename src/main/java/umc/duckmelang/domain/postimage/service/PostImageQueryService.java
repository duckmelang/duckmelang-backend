package umc.duckmelang.domain.postimage.service;

import org.springframework.data.domain.Page;
import umc.duckmelang.domain.postimage.dto.PostThumbnailResponseDto;

public interface PostImageQueryService {
    Page<PostThumbnailResponseDto> getPostsImage(Long memberId, int page);
}
