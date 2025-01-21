package umc.duckmelang.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import umc.duckmelang.domain.postimage.converter.PostImageConverter;
import umc.duckmelang.domain.postimage.dto.PostImageResponseDto;
import umc.duckmelang.domain.postimage.dto.PostThumbnailResponseDto;
import umc.duckmelang.domain.postimage.service.PostImageQueryService;
import umc.duckmelang.global.apipayload.ApiResponse;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {
    private final PostImageQueryService postImageQueryService;
    @GetMapping(path = "/posts")
    @Operation(summary = "내가 업로드한 게시글들 조회",description = "memberId는 추후 jwt로 바꿉니다\n게시글 썸네일 이미지(postImageUrl), 게시글 클릭 시 서버에 전달할 게시글 식별자(postId), 게시글 이미지 생성시점(createdAt) 반환합니다.")
    ApiResponse<PostImageResponseDto.PostThumbnailListResponseDto> getPostImages(@RequestParam Long memberId, int page) {
        Page<PostThumbnailResponseDto> postThumbnailResponseDtoPage = postImageQueryService.getMyPostsImage(memberId, page);
        return ApiResponse.onSuccess(PostImageConverter.toPostThumbnailListResponseDto(postThumbnailResponseDtoPage));
    }
}
