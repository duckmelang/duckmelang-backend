package umc.duckmelang.domain.memberprofileimage.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;

public interface MemberProfileImageRepository extends JpaRepository<MemberProfileImage, Long> {
    Optional<MemberProfileImage> findFirstByMemberIdOrderByCreatedAtDesc(@Param("memberId") Long memberId);

    Page<MemberProfileImage> findAllByMemberId(Long memberId, Pageable pageable);
}
