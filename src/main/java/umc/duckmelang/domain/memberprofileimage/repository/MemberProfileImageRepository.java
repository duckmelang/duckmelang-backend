package umc.duckmelang.domain.memberprofileimage.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;

@Repository
public interface MemberProfileImageRepository extends JpaRepository<MemberProfileImage, Long> {
    Optional<MemberProfileImage> findFirstByMemberIdAndIsPublicTrueOrderByCreatedAtDesc(Long memberId);
    Page<MemberProfileImage> findAllByMemberIdAndMemberImageNot(Long memberId, String memberImage, Pageable pageable);
    Page<MemberProfileImage> findAllByIsPublicIsTrueAndMemberIdAndMemberImageNot(Long memberId, String memberImage, Pageable pageable);
}
