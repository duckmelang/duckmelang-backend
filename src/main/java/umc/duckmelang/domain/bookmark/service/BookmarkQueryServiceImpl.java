package umc.duckmelang.domain.bookmark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.duckmelang.domain.bookmark.repository.BookmarkRepository;
import umc.duckmelang.domain.post.domain.Post;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkQueryServiceImpl implements BookmarkQueryService {
    private final BookmarkRepository bookmarkRepository;

    @Override
    public Page<Post> getBookmarks(Long memberId, Integer page){
        return bookmarkRepository.findBookmarks(memberId, PageRequest.of(page, 10));
    }


}
