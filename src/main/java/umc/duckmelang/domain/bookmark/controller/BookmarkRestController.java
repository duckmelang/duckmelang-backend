package umc.duckmelang.domain.bookmark.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import umc.duckmelang.domain.bookmark.converter.BookmarkConverter;
import umc.duckmelang.domain.bookmark.domain.Bookmark;
import umc.duckmelang.domain.bookmark.dto.BookmarkResponseDto;
import umc.duckmelang.domain.bookmark.service.BookmarkCommandService;
import umc.duckmelang.domain.bookmark.service.BookmarkQueryService;
import umc.duckmelang.domain.post.converter.PostConverter;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.dto.PostResponseDto;
import umc.duckmelang.global.validation.annotation.ValidPageNumber;
import umc.duckmelang.global.annotations.CommonApiResponses;
import umc.duckmelang.global.apipayload.ApiResponse;

@RestController
@RequiredArgsConstructor
@Validated
public class BookmarkRestController {

    private final BookmarkQueryService bookmarkQueryService;
    private final BookmarkCommandService bookmarkCommandService;

    @GetMapping("/bookmarks/{memberId}")
    @CommonApiResponses
    @Operation(summary = "스크랩 내역 조회 API", description = "개인 스크랩 내역 조회 API입니다. 페이징을 포함하며 한 페이지 당 10개 게시글을 보여줍니다. query String으로 page 번호를 주세요. page 번호는 0부터 시작합니다")
    public ApiResponse<PostResponseDto.PostPreviewListDto> getBookmarks(@PathVariable(name="memberId") Long memberId, @ValidPageNumber @RequestParam(name = "page",  defaultValue = "0") Integer page) {
        Page<Post> postList = bookmarkQueryService.getBookmarks(memberId, page);
        return ApiResponse.onSuccess(PostConverter.postPreviewListDto(postList));
    }

    @PostMapping("posts/{postId}/bookmarks")
    @CommonApiResponses
    @Operation(summary = "게시글 스크랩 API", description = "게시글 스크랩하는 API 입니다. 우선 memberId를 받습니다(추후 JWT로 변경 예정)")
    public ApiResponse<BookmarkResponseDto.BookmarkJoinResultDto>joinBookmark (@PathVariable(name="postId") Long postId, @RequestParam(name="memberId") Long memberId){
        Bookmark bookmark = bookmarkCommandService.joinBookmark(postId, memberId);
        return ApiResponse.onSuccess(BookmarkConverter.bookmarkJoinResultDto(bookmark));
    }

}
