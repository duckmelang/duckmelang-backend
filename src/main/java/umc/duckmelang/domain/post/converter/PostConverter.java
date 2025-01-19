package umc.duckmelang.domain.post.converter;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.dto.PostResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostConverter {

    public static PostResponseDto.PostPreviewDto postPreviewDto(Post post) {
        return PostResponseDto.PostPreviewDto.builder()
                .title(post.getTitle())
                .category(post.getEventCategory().getName())
                .date(post.getEventDate())
                .name(post.getMember().getName())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public static PostResponseDto.PostPreviewListDto postPreviewListDto(Page<Post> postList) {
        List<PostResponseDto.PostPreviewDto> postPreviewDtoList = postList.stream()
                .map(PostConverter::postPreviewDto).collect(Collectors.toList());

        return PostResponseDto.PostPreviewListDto.builder()
                .isLast(postList.isLast())
                .isFirst(postList.isFirst())
                .totalPage(postList.getTotalPages())
                .totalElements(postList.getTotalElements())
                .listSize(postPreviewDtoList.size())
                .postList(postPreviewDtoList)
                .build();
    }

    public static PostResponseDto.PostDetailDto postDetailDto(Post post) {

        List<String> idolNames = post.getPostIdolList().stream()
                .map(postIdol -> postIdol.getIdolCategory().getName())
                .collect(Collectors.toList());

        return PostResponseDto.PostDetailDto.builder()
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
