package umc.duckmelang.domain.postimage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.postimage.dto.PostImageResponseDto;
import umc.duckmelang.domain.postimage.dto.PostThumbnailResponseDto;
import umc.duckmelang.domain.postimage.repository.PostImageRepository;

@Service
@RequiredArgsConstructor
public class PostImageQueryServiceImpl implements PostImageQueryService {
    private final PostImageRepository postImageRepository;

    //프로필에서 대표로 보일 이미지 조회
    @Override
    public Page<PostThumbnailResponseDto> getPostsImage(Long memberId, int page) {
        return postImageRepository.findLatestPostImagesForMemberPosts(memberId, PageRequest.of(page, 10));
    }

    @Override
    public Page<PostThumbnailResponseDto> getMyPostsImage(Long memberId, int page) {
        return postImageRepository.findLatestPostImagesForMemberPosts(memberId, PageRequest.of(page, 10));
    }
}
