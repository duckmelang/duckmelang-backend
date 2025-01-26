package umc.duckmelang.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

public class PostRequestDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PostJoinDto{
        @NotBlank
        private String title;
        @NotBlank
        private String content;
        @NotNull
        private List<Long> idolIds;
        @NotNull
        private Long categoryId;
        @NotNull
        private LocalDate date;
    }
}
