package umc.duckmelang.domain.bookmark.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import umc.duckmelang.domain.bookmark.dto.BookmarkResponseDto;
import umc.duckmelang.domain.bookmark.service.BookmarkQueryService;
import umc.duckmelang.domain.idolcategory.validation.annotation.ExistIdol;
import umc.duckmelang.domain.post.converter.PostConverter;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.dto.PostResponseDto;
import umc.duckmelang.global.apipayload.ApiResponse;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/bookmarks")
public class BookmarkController {

    private final BookmarkQueryService bookmarkQueryService;

    @GetMapping("/{memberId}")
    @Operation(summary = "스크랩 내역 조회 API", description = "개인 스크랩 내역 조회 API입니다. 페이징을 포함하며 한 페이지 당 10개 게시글을 보여줍니다. query String으로 page 번호를 주세요. page 번호는 0부터 시작합니다")
    public ApiResponse<PostResponseDto.PostPreviewListDto> getBookmarks(@PathVariable(name="memberId") Long memberId, @RequestParam(name = "page") Integer page) {
        Page<Post> postList = bookmarkQueryService.getBookmarks(memberId, page);
        return ApiResponse.onSuccess(PostConverter.postPreviewListDto(postList));
    }

}
