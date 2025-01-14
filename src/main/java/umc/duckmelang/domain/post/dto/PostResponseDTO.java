package umc.duckmelang.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PostPreviewDTO{
        private String title;
        private String category;
        private LocalDate date;
        private String name;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PostPreviewListDTO{
        List<PostPreviewDTO> postList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PostDetailDTO{
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
}
