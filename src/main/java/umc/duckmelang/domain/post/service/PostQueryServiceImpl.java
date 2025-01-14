package umc.duckmelang.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class PostQueryServiceImpl implements PostQueryService{

    private final PostRepository postRepository;

    @Override
    public Page<Post> getPostList(Integer page){
        return postRepository.findAll(PageRequest.of(page, 10));
    }


}
