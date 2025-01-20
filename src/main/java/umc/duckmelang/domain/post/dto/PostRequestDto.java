package umc.duckmelang.domain.post.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

public class PostRequestDto {

    @Getter
    @Setter
    public static class PostJoinDto{
        @NotNull
        private String title;
        @NotNull
        private String content;
        @NotNull
        private List<Long> idolIds;
        @NotNull
        private Long categoryId;
        @NotNull
        private LocalDate date;
    }
}
