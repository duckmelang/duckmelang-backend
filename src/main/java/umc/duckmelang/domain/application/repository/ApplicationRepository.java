package umc.duckmelang.domain.application.repository;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.duckmelang.domain.application.domain.Application;
import umc.duckmelang.domain.application.domain.enums.ApplicationStatus;


public interface ApplicationRepository extends JpaRepository<Application, Long> {
    @EntityGraph(attributePaths = {"member","post", "post.member"})
    Optional<Application> findByIdAndPostMemberId(Long id, Long memberId);

    Optional<Application> findByIdAndMemberId(Long id, Long memberId);

    boolean existsByPostIdAndStatus(Long postId, ApplicationStatus status);
    @EntityGraph(attributePaths = {"member","post"})
    boolean existsByPostIdAndMemberId(Long postId, Long memberId);

    @Query("SELECT a FROM Post p " +
            "JOIN p.member pm " +
            "INNER JOIN p.applicationList a " +
            "INNER JOIN a.member m " +
            "WHERE a.status = :status " +
            "AND pm.id = :postOwnerId")
    Page<Application> findReceivedApplicationListByStatus(
            @Param("postOwnerId") Long postOwnerId,
            @Param("status") ApplicationStatus status,
            Pageable pageable
    );

    @EntityGraph(attributePaths = "member")
    @Query(value = "SELECT a FROM Application a " +
            "WHERE a.member.id = :applicationOwnerId")
    Page<Application> findSentApplicationList(
            @Param("applicationOwnerId") Long applicationOwnerId,
            Pageable pageable
    );

    @Query("SELECT a FROM Post p " +
            "JOIN p.member pm " +
            "INNER JOIN p.applicationList a " +
            "INNER JOIN a.member m " +
            "WHERE pm.id = :postOwnerId " +
            "AND a.status <> umc.duckmelang.domain.application.domain.enums.ApplicationStatus.PENDING")
    Page<Application> findReceivedApplicationListExceptPending(
            @Param("postOwnerId") Long postOwnerId,
            Pageable pageable
    );

    @Query(value = "SELECT a FROM Application a " +
            "INNER JOIN a.member m " +
            "WHERE m.id = :applicationOwnerId " +
            "AND a.status = :status")
    Page<Application> findSentApplicationListByStatus(
            @Param("applicationOwnerId") Long applicationOwnerId,
            @Param("status") ApplicationStatus status,
            Pageable pageable
    );

    @Query("SELECT COUNT(a) FROM Application a " +
            "WHERE (a.member.id = :memberId OR a.post.member.id = :memberId) " +
            "AND a.status = :status")
    int countByMemberIdOrPostMemberIdAndStatus(@Param("memberId") Long memberId,
                                               @Param("status") ApplicationStatus status);}
