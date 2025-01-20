package umc.duckmelang.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDto {

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
        List<PostPreviewDto> postList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PostDetailDto{
        private String name;
        private LocalDate birth;
        private Boolean gender;
        private String title;
        private String content;
        private List<String> idol;
        private String category;
        private LocalDate date;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PostJoinResultDto{
        private String title;

    }
}
