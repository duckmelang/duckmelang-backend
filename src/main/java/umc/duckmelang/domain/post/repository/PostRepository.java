package umc.duckmelang.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import umc.duckmelang.domain.member.domain.enums.Gender;
import umc.duckmelang.domain.post.domain.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE " +
            "(:gender IS NULL OR p.member.gender = :gender) AND " +
            "(:minAge IS NULL OR (YEAR(CURRENT_DATE) - YEAR(p.member.birth)) >= :minAge) AND " +
            "(:maxAge IS NULL OR (YEAR(CURRENT_DATE) - YEAR(p.member.birth)) <= :maxAge)")
    Page<Post> findFilteredPosts(@Param("gender") Gender gender,
                                 @Param("minAge") Integer minAge,
                                 @Param("maxAge") Integer maxAge,
                                 Pageable pageable);


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
