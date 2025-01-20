package umc.duckmelang.domain.post.converter;

import jdk.jfr.Event;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import umc.duckmelang.domain.eventcategory.domain.EventCategory;
import umc.duckmelang.domain.idolcategory.domain.IdolCategory;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.dto.PostRequestDto;
import umc.duckmelang.domain.post.dto.PostResponseDto;
import umc.duckmelang.domain.postidol.domain.PostIdol;

import java.util.ArrayList;
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

    public static PostResponseDto.PostJoinResultDto postJoinResultDto(Post post) {
        return PostResponseDto.PostJoinResultDto.builder()
                .title(post.getTitle())
                .build();
    }

    public static Post toPost(PostRequestDto.PostJoinDto request, Member member, EventCategory eventCategory, List<IdolCategory> idolCategories){
        Post post =  Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .eventDate(request.getDate())
                .eventCategory(eventCategory)
                .wanted((short)1)
                .member(member)
                .build();

//        아이돌
        List<PostIdol> postIdols = idolCategories.stream()
                .map(idolCategory -> PostIdol.builder()
                        .post(post)
                        .idolCategory(idolCategory)
                        .build())
                .toList();

        if (post.getPostIdolList() == null) {
            post.setPostIdolList(new ArrayList<>());
        }

        post.getPostIdolList().addAll(postIdols);

        return post;
    }



}
