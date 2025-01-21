package umc.duckmelang.domain.application.repository;

import java.time.LocalDateTime;
import java.util.*;

import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.duckmelang.domain.application.domain.Application;
import umc.duckmelang.domain.application.domain.enums.ApplicationStatus;
import umc.duckmelang.domain.application.dto.ReceivedApplicationDto;
import umc.duckmelang.domain.application.dto.SentApplicationDto;


public interface ApplicationRepository extends JpaRepository<Application, Long> {
    @EntityGraph(attributePaths = {"member","post", "post.member"})
    Optional<Application> findByIdAndPostMemberId(Long id, Long memberId);

    Optional<Application> findByIdAndMemberId(Long id, Long memberId);

    @Query("SELECT new umc.duckmelang.domain.application.dto.ReceivedApplicationDto(" +
            "m.name, " +
            "p.id, " +
            "p.title, " +
            "a.id, " +
            "a.createdAt) " +
            "FROM Post p " +
            "JOIN p.member pm " +
            "INNER JOIN p.applicationList a " +
            "INNER JOIN a.member m " +
            "WHERE a.status = :status " +
            "AND pm.id = :postOwnerId")
    Page<ReceivedApplicationDto> findReceivedApplicationList(
            @Param("postOwnerId") Long postOwnerId,
            @Param("status") ApplicationStatus status,
            Pageable pageable
    );

    @Query(value = "SELECT new umc.duckmelang.domain.application.dto.SentApplicationDto(" +
            "p.id, " +
            "p.title, " +
            "pm.nickname, " +
            "p.eventDate, " +  // LocalDateTime -> LocalDate 변환은 DTO 내부에서 처리
            "e.name, " +
            "a.id) " +
            "FROM Application a " +
            "INNER JOIN a.member m " +
            "INNER JOIN a.post p " +
            "INNER JOIN p.member pm " +
            "INNER JOIN p.eventCategory e " +
            "WHERE m.id = :applicationOwnerId " +
            "AND a.status = :status")
    Page<SentApplicationDto> findSentApplicationList(
            @Param("applicationOwnerId") Long applicationOwnerId,
            @Param("status") ApplicationStatus status,
            Pageable pageable
    );

    @Query("SELECT COUNT(a) FROM Application a " +
            "WHERE (a.member.id = :memberId OR a.post.member.id = :memberId) " +
            "AND a.status = :status")
    int countByMemberIdOrPostMemberIdAndStatus(@Param("memberId") Long memberId,
                                                @Param("status") ApplicationStatus status);
}
