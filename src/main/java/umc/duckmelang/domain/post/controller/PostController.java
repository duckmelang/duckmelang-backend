package umc.duckmelang.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import umc.duckmelang.domain.post.converter.PostConverter;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.dto.PostResponseDTO;
import umc.duckmelang.domain.post.service.PostQueryService;
import umc.duckmelang.domain.postidol.domain.PostIdol;
import umc.duckmelang.global.apipayload.ApiResponse;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostQueryService postQueryService;

    @GetMapping("/")
    @Operation(summary = "홈화면 게시글 전체 조회 API", description = "조건 없이 모든 게시글을 조회하는 API 입니다. 페이징을 포함하며 한 페이지 당 10개 게시글을 보여줍니다. query String으로 page 번호를 주세요. page 번호는 0부터 시작합니다")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "acess 토큰 만료",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "acess 토큰 모양이 이상함",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<PostResponseDTO.PostPreviewListDTO> getPostList (@RequestParam(name = "page") Integer page){
        Page<Post> postList = postQueryService.getPostList(page);
        return ApiResponse.onSuccess(PostConverter.postPreviewListDTO(postList));
    }

    @GetMapping("/idol/{idolId}")
    @Operation(summary = "홈화면 게시글 아이돌 기반 조회 API", description = "해당하는 아이돌의 글만 조회하는 API 입니다. 페이징을 포함하며 한 페이지 당 10개 게시글을 보여줍니다. query String으로 page 번호를 주세요. page 번호는 0부터 시작합니다")
    @Parameters({
            @Parameter(name = "idolId", description = "아이돌 Id, path variable 입니다!")
    })
    public ApiResponse<PostResponseDTO.PostPreviewListDTO> getPostListByIdol (@PathVariable(name="idolId") Long idolId, @RequestParam(name = "page") Integer page){
        Page<Post> postList = postQueryService.getPostListByIdol(idolId, page);
        return ApiResponse.onSuccess(PostConverter.postPreviewListDTO(postList));
    }

    @GetMapping("/{postId}")
    @Operation(summary = "게시글 상세 조회 API", description = "홈화면에서 게시글 1개 클릭시 자세히 보여주는 API입니다. 성별은 true일때 남자, false일때 여자입니다. 스크랩, 채팅, 조회와 원하는 조건은 아직 만들지 않았음")
    @Parameters({@Parameter(name = "postId", description = "게시글 Id, path variable 입니다")})
    public ApiResponse<PostResponseDTO.PostDetailDTO> getPostDetail (@PathVariable(name="postId") Long postId){
        Post post = postQueryService.getPostDetail(postId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 존재하지 않습니다"));
        return ApiResponse.onSuccess(PostConverter.postDetailDTO(post));
    }

}
