package umc.duckmelang.domain.post.converter;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.dto.PostResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostConverter {

    public static PostResponseDTO.PostPreviewDTO postPreviewDTO(Post post) {
        return PostResponseDTO.PostPreviewDTO.builder()
                .title(post.getTitle())
                .category(post.getEventCategory().getName())
                .date(post.getEventDate())
                .name(post.getMember().getName())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public static PostResponseDTO.PostPreviewListDTO postPreviewListDTO(Page<Post> postList) {
        List<PostResponseDTO.PostPreviewDTO> postPreviewDTOList = postList.stream()
                .map(PostConverter::postPreviewDTO).collect(Collectors.toList());

        return PostResponseDTO.PostPreviewListDTO.builder()
                .isLast(postList.isLast())
                .isFirst(postList.isFirst())
                .totalPage(postList.getTotalPages())
                .totalElements(postList.getTotalElements())
                .listSize(postPreviewDTOList.size())
                .postList(postPreviewDTOList)
                .build();
    }

    public static PostResponseDTO.PostDetailDTO postDetailDTO(Post post) {

        List<String> idolNames = post.getPostIdolList().stream()
                .map(postIdol -> postIdol.getIdolCategory().getName())
                .collect(Collectors.toList());

        return PostResponseDTO.PostDetailDTO.builder()
                .name(post.getMember().getName())
                .birth(post.getMember().getBirth())
                .gender(post.getMember().getGender())
                .title(post.getTitle())
                .content(post.getContent())
                .idol(idolNames)
                .category(post.getEventCategory().getName())
                .date(post.getEventDate())
                .createdAt(post.getCreatedAt())
                .build();

    }



}
