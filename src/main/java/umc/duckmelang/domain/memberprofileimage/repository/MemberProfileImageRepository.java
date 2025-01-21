package umc.duckmelang.domain.memberprofileimage.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import java.util.List;
public interface MemberProfileImageRepository extends JpaRepository<MemberProfileImage, Long> {
    @Query(value = "SELECT m FROM MemberProfileImage m WHERE m.member.id = :memberId ORDER BY m.createdAt DESC LIMIT 1")
    Optional<MemberProfileImage> findTopByMemberIdOrderByCreatedAtDesc(@Param("memberId") Long memberId);

    Page<MemberProfileImage> findAllByMemberId(Long memberId, Pageable pageable);
}
