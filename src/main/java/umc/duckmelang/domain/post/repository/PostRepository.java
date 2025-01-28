package umc.duckmelang.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import umc.duckmelang.domain.idolcategory.domain.IdolCategory;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.postidol.domain.PostIdol;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"postIdolList", "postIdolList.idolCategory"})
    @Query("SELECT p FROM Post p JOIN p.postIdolList pi JOIN pi.idolCategory ic WHERE ic.id = :idolId")
    Page<Post> findByIdol(@Param("idolId") Long idolId, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN FETCH p.member pm WHERE pm.id = :memberId")
    Page<Post> findByMember(@Param("memberId") Long memberId, Pageable pageable);


    @Query("SELECT p FROM Post p WHERE p.title LIKE %:searchKeyword%")
    Page<Post> findByTitle(String searchKeyword, Pageable pageable);

    @EntityGraph(attributePaths = {"member"})
    @Query("SELECT p FROM Post p WHERE p.member.id = :memberId")
    Page<Post> findMyPost(Long memberId, Pageable pageable);

    int countAllByMemberId(Long memberId);

}
