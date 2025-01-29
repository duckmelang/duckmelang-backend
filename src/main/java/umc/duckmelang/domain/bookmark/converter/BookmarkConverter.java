package umc.duckmelang.domain.bookmark.converter;

import org.springframework.stereotype.Component;
import umc.duckmelang.domain.bookmark.domain.Bookmark;
import umc.duckmelang.domain.bookmark.dto.BookmarkResponseDto;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.post.domain.Post;

@Component
public class BookmarkConverter {
    public static BookmarkResponseDto.BookmarkJoinResultDto bookmarkJoinResultDto(Bookmark bookmark) {
        return BookmarkResponseDto.BookmarkJoinResultDto.builder()
                .bookmarkId(bookmark.getId())
                .memberId(bookmark.getMember().getId())
                .postId(bookmark.getPost().getId())
                .build();
    }

    public static Bookmark toBookmark(Member member, Post post) {
        return Bookmark.builder()
                .member(member)
                .post(post)
                .build();

    }
}