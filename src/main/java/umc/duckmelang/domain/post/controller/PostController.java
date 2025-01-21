package umc.duckmelang.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import umc.duckmelang.domain.idolcategory.validation.annotation.ExistIdol;
import umc.duckmelang.domain.post.converter.PostConverter;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.dto.PostRequestDto;
import umc.duckmelang.domain.post.dto.PostResponseDto;
import umc.duckmelang.domain.post.service.PostCommandService;
import umc.duckmelang.domain.post.service.PostCommandServiceImpl;
import umc.duckmelang.domain.post.service.PostQueryService;
import umc.duckmelang.global.annotations.CommonApiResponses;
import umc.duckmelang.global.apipayload.ApiResponse;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Validated
public class PostController {

    private final PostQueryService postQueryService;
    private final PostCommandService postCommandService;

    @GetMapping("/")
    @CommonApiResponses
    @Operation(summary = "홈화면 게시글 전체 조회 API", description = "조건 없이 모든 게시글을 조회하는 API 입니다. 페이징을 포함하며 한 페이지 당 10개 게시글을 보여줍니다. query String으로 page 번호를 주세요. page 번호는 0부터 시작합니다")
    public ApiResponse<PostResponseDto.PostPreviewListDto> getPostList (@RequestParam(name = "page") Integer page){
        if(page<0){
            throw new IllegalArgumentException("페이지 번호는 0 이상어야합니다");
        }
        Page<Post> postList = postQueryService.getPostList(page);
        return ApiResponse.onSuccess(PostConverter.postPreviewListDto(postList));
    }

    @GetMapping("/idol/{idolId}")
    @CommonApiResponses
    @Operation(summary = "홈화면 게시글 아이돌 기반 조회 API", description = "해당하는 아이돌의 글만 조회하는 API 입니다. 페이징을 포함하며 한 페이지 당 10개 게시글을 보여줍니다. query String으로 page 번호를 주세요. page 번호는 0부터 시작합니다")
    @Parameters({
            @Parameter(name = "idolId", description = "아이돌 Id, path variable 입니다!")
    })
    public ApiResponse<PostResponseDto.PostPreviewListDto> getPostListByIdol (@ExistIdol @PathVariable(name="idolId") Long idolId, @RequestParam(name = "page") Integer page){
        if(page<0){
            throw new IllegalArgumentException("페이지 번호는 0 이상어야합니다");
        }
        Page<Post> postList = postQueryService.getPostListByIdol(idolId, page);
        return ApiResponse.onSuccess(PostConverter.postPreviewListDto(postList));
    }

    @GetMapping("/{postId}")
    @CommonApiResponses
    @Operation(summary = "게시글 상세 조회 API", description = "홈화면에서 게시글 1개 클릭시 자세히 보여주는 API입니다. 성별은 true일때 남자, false일때 여자입니다. 스크랩, 채팅, 조회수, 후기평 아직 만들지 않음")
    @Parameters({@Parameter(name = "postId", description = "게시글 Id, path variable 입니다")})
    public ApiResponse<PostResponseDto.PostDetailDto> getPostDetail (@PathVariable(name="postId") Long postId){
        Post post = postQueryService.getPostDetail(postId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 존재하지 않습니다"));
        return ApiResponse.onSuccess(PostConverter.postDetailDto(post));
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
    public ApiResponse<PostResponseDto.PostPreviewListDto> getPostListByTitle (@RequestParam(name = "page") Integer page, @RequestParam(name="searchKeyword") String searchKeyword){
        Page<Post> postList = postQueryService.getPostListByTitle(searchKeyword, page);
        return ApiResponse.onSuccess(PostConverter.postPreviewListDto(postList));


    }

}
