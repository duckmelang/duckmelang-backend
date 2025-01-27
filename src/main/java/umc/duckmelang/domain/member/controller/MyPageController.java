package umc.duckmelang.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import umc.duckmelang.domain.post.converter.PostConverter;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.dto.PostResponseDto;
import umc.duckmelang.domain.post.service.PostQueryService;

import umc.duckmelang.global.apipayload.ApiResponse;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {
    private final PostQueryService postQueryService;
    @GetMapping(path = "/posts")
    @Operation(summary = "내가 업로드한 게시글들 조회",description = "memberId는 추후 jwt로 바꿉니다")
    ApiResponse<PostResponseDto.PostPreviewListDto> getMyPostList(@RequestParam Long memberId, int page) {
        if(page<0){
            throw new IllegalArgumentException("페이지 번호는 0 이상어야합니다");
        }
        Page<Post> postList = postQueryService.getPostListByMember(memberId, page);
        return ApiResponse.onSuccess(PostConverter.postPreviewListDto(postList));
    }
}
