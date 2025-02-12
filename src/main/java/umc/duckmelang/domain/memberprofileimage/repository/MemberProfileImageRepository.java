package umc.duckmelang.domain.memberprofileimage.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;

@Repository
public interface MemberProfileImageRepository extends JpaRepository<MemberProfileImage, Long> {
    void deleteAllByMember(Member member);
    Optional<MemberProfileImage> findFirstByMemberIdAndIsPublicTrueOrderByCreatedAtAsc(Long memberId);
    Page<MemberProfileImage> findAllByMemberId(long memberId, Pageable pageable);
    Page<MemberProfileImage> findAllByIsPublicIsTrueAndMemberId(long memberId, Pageable pageable);
}
