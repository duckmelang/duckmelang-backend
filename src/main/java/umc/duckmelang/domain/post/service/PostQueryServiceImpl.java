package umc.duckmelang.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.repository.PostRepository;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.MemberException;
import umc.duckmelang.global.apipayload.exception.PostException;

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
    public Page<Post> getPostListByMember(Long memberId, Integer page) {
        return postRepository.findByMember(memberId, PageRequest.of(page,10));
    }

    @Override
    public Optional<Post> getPostDetail(Long postId){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorStatus.POST_NOT_FOUND));
        post.increaseViewCount();
        postRepository.save(post);
        return Optional.of(post);
    }

    @Override
    public Page<Post> getPostListByTitle(String searchKeyword, Integer page){
        return postRepository.findByTitle(searchKeyword, PageRequest.of(page, 10));
    }

    @Override
    public Optional<Post> findById(Long postId) {
        return postRepository.findById(postId);
    }

    @Override
    public Page<Post> getMyPostList(Long memberId, Integer page){
        return postRepository.findMyPost(memberId, PageRequest.of(page, 10));
    }

    /**
     * 특정 멤버가 작성한 게시물 수 조회: 프로필 조회 시 사용
     *
     * @param memberId 멤버 ID
     * @return 게시물 수
     */
    @Override
    public int getPostCount(Long memberId) {
        return postRepository.countAllByMemberId(memberId);
    }




}
