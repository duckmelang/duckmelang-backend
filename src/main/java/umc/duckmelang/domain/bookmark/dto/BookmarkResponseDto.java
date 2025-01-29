package umc.duckmelang.domain.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BookmarkResponseDto {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookmarkJoinResultDto{
        private Long bookmarkId;
        private Long memberId;
        private Long postId;
    }
}
