package umc.duckmelang.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostPreviewDto{
        private Long postId;
        private String title;
        private String category;
        private LocalDate date;
        private String nickname;
        private LocalDateTime createdAt;
        private String postImageUrl;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
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
    @NoArgsConstructor
    public static class PostDetailDto{
        private String nickname;
        private Integer age;
        private String gender;
        private String title;
        private String content;
        private List<String> idol;
        private String category;
        private LocalDate date;
        private LocalDateTime createdAt;
        private List<String> postImageUrl;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostJoinResultDto{
        private Long id;
        private String title;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostStatusDto{
        private Long id;
        private String title;
        private Short wanted;
    }

}
