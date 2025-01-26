package umc.duckmelang.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryServiceImpl implements PostQueryService{

    private final PostRepository postRepository;

    @Override
    public Page<Post> getPostList(Integer page){
        return postRepository.findAll(PageRequest.of(page, 10));
    }

    @Override
    public Page<Post> getPostListByIdol(Long idolId, Integer page){
        return postRepository.findByIdol(idolId, PageRequest.of(page, 10));
    }

    @Override
    public Optional<Post> getPostDetail(Long postId){
        return postRepository.findById(postId);
    }

    @Override
    public Page<Post> getPostListByTitle(String searchKeyword, Integer page){
        return postRepository.findByTitle(searchKeyword, PageRequest.of(page, 10));

    }

    @Override
    public Optional<Post> findById(Long postId){
        return postRepository.findById(postId);
    }

}
