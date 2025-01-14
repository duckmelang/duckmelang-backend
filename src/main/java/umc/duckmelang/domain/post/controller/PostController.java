package umc.duckmelang.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import umc.duckmelang.global.apipayload.ApiResponse;

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
}
