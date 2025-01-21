package umc.duckmelang.domain.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import umc.duckmelang.domain.post.dto.PostResponseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BookmarkResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PostPreviewDto{
        private String title;
        private String category;
        private LocalDate date;
        private String name;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PostPreviewListDto{
        List<PostResponseDto.PostPreviewDto> postList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }
}
