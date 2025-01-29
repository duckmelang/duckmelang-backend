package umc.duckmelang.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import umc.duckmelang.domain.bookmark.converter.BookmarkConverter;
import umc.duckmelang.domain.bookmark.domain.Bookmark;
import umc.duckmelang.domain.bookmark.dto.BookmarkResponseDto;
import umc.duckmelang.domain.bookmark.service.BookmarkCommandService;
import umc.duckmelang.domain.bookmark.service.BookmarkQueryService;
import umc.duckmelang.domain.bookmark.service.BookmarkQueryServiceImpl;
import umc.duckmelang.domain.idolcategory.validation.annotation.ExistIdol;
import umc.duckmelang.domain.post.converter.PostConverter;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.dto.PostRequestDto;
import umc.duckmelang.domain.post.dto.PostResponseDto;
import umc.duckmelang.domain.post.repository.PostRepository;
import umc.duckmelang.domain.post.service.PostCommandService;
import umc.duckmelang.domain.post.service.PostCommandServiceImpl;
import umc.duckmelang.domain.post.service.PostQueryService;
import umc.duckmelang.domain.post.validation.annotation.ExistPost;
import umc.duckmelang.domain.post.validation.annotation.ValidPageNumber;
import umc.duckmelang.domain.review.domain.Review;
import umc.duckmelang.domain.review.service.ReviewQueryService;
import umc.duckmelang.global.annotations.CommonApiResponses;
import umc.duckmelang.global.apipayload.ApiResponse;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Validated
public class PostController {

    private final PostQueryService postQueryService;
    private final PostCommandService postCommandService;
    private final ReviewQueryService reviewQueryService;
    private final BookmarkQueryService bookmarkQueryService;
    private final PostRepository postRepository;

    @GetMapping("")
    @CommonApiResponses
    @Operation(summary = "홈화면 게시글 전체 조회 API", description = "조건 없이 모든 게시글을 조회하는 API 입니다. 페이징을 포함하며 한 페이지 당 10개 게시글을 보여줍니다. query String으로 page 번호를 주세요. page 번호는 0부터 시작합니다")
    public ApiResponse<PostResponseDto.PostPreviewListDto> getPostList (@ValidPageNumber @RequestParam(name = "page",  defaultValue = "0") Integer page){
        Page<Post> postList = postQueryService.getPostList(page);
        return ApiResponse.onSuccess(PostConverter.postPreviewListDto(postList));
    }

    @GetMapping("/idol/{idolId}")
    @CommonApiResponses
    @Operation(summary = "홈화면 게시글 아이돌 기반 조회 API", description = "해당하는 아이돌의 글만 조회하는 API 입니다. 페이징을 포함하며 한 페이지 당 10개 게시글을 보여줍니다. query String으로 page 번호를 주세요. page 번호는 0부터 시작합니다")
    @Parameters({
            @Parameter(name = "idolId", description = "아이돌 Id, path variable 입니다!")
    })
    public ApiResponse<PostResponseDto.PostPreviewListDto> getPostListByIdol (@ExistIdol @PathVariable(name="idolId") Long idolId, @ValidPageNumber @RequestParam(name = "page",  defaultValue = "0") Integer page){
        Page<Post> postList = postQueryService.getPostListByIdol(idolId, page);
        return ApiResponse.onSuccess(PostConverter.postPreviewListDto(postList));
    }

    @GetMapping("/{postId}")
    @CommonApiResponses
    @Operation(summary = "게시글 상세 조회 API", description = "홈화면에서 게시글 1개 클릭시 자세히 보여주는 API입니다. wanted가 0이면 종료, 1이면 진행 중입니다.  채팅수 조회 아직 만들지 않음")
    @Parameters({@Parameter(name = "postId", description = "게시글 Id, path variable 입니다")})
    public ApiResponse<PostResponseDto.PostDetailDto> getPostDetail (@ExistPost @PathVariable(name="postId") Long postId){
        Post post = postQueryService.getPostDetail(postId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 존재하지 않습니다"));
        List<Review> reviewList = Optional.ofNullable(reviewQueryService.getReviewList(post.getMember().getId()))
                .orElse(Collections.emptyList());
        double averageScore = reviewQueryService.calculateAverageScore(reviewList);
        Integer bookmarkCount = bookmarkQueryService.getBookmarkCount(postId);
        return ApiResponse.onSuccess(PostConverter.postDetailDto(post, averageScore, bookmarkCount));
    }

    @PostMapping("/{memberId}")
    @CommonApiResponses
    @Operation(summary = "게시글 작성 API", description = "게시글 쓰기 API입니다. ")
    public ApiResponse<PostResponseDto.PostJoinResultDto> joinPost (@PathVariable(name="memberId") Long memberId, @RequestBody @Valid PostRequestDto.PostJoinDto request){
        Post post = postCommandService.joinPost(request, memberId);
        return ApiResponse.onSuccess(PostConverter.postJoinResultDto(post));
    }

    @GetMapping("/search")
    @CommonApiResponses
    @Operation(summary = "게시글 검색 API", description = "게시글 검색 API입니다. title 기준으로 검색합니다. 페이징을 포함하며 한 페이지 당 10개 게시글을 보여줍니다. query String으로 page 번호를 주세요. page 번호는 0부터 시작합니다")
    public ApiResponse<PostResponseDto.PostPreviewListDto> getPostListByTitle (@ValidPageNumber @RequestParam(name = "page",  defaultValue = "0") Integer page, @RequestParam(name="searchKeyword") String searchKeyword){
        Page<Post> postList = postQueryService.getPostListByTitle(searchKeyword, page);
        return ApiResponse.onSuccess(PostConverter.postPreviewListDto(postList));

    }


    @GetMapping("/my")
    @CommonApiResponses
    @Operation(summary = "내 게시글 조회 API", description = "나의 동행에서 내 게시글을 확인하는 API입니다. memberId를 받고, 추후 JWT로 변경예정, 페이징을 포함하며 한 페이지 당 10개 게시글을 보여줍니다. query String으로 page 번호를 주세요. page 번호는 0부터 시작합니다")
    public ApiResponse<PostResponseDto.PostPreviewListDto> getMyPostList(@RequestParam("memberId") Long memberId, @ValidPageNumber @RequestParam(name ="page", defaultValue = "0") Integer page){
        Page<Post> postList = postQueryService.getMyPostList(memberId, page);
        return ApiResponse.onSuccess(PostConverter.postPreviewListDto(postList));
    }

    @PatchMapping("/{postId}/status")
    @CommonApiResponses
    @Operation(summary = "게시글 상태 변경 API", description = "게시글을 상태를 모집 중 -> 모집 완료/ 또는 모집 완료 -> 모집 중으로 바꿉니다. 모집 중은 wanted가 1, 모집 완료는 0입니다.")
    public ApiResponse<PostResponseDto.PostStatusDto> patchPostStatus(@ExistPost @PathVariable("postId") Long postId){
        Post post = postCommandService.patchPostStatus(postId);
        return ApiResponse.onSuccess(PostConverter.postStatusDto(post));
    }

}
