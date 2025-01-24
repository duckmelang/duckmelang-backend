package umc.duckmelang.domain.memberprofileimage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberProfileImageRepository extends JpaRepository<MemberProfileImage, Long> {
    void deleteAllByMember(Member member);
    Optional<MemberProfileImage> findFirstByMemberIdAndIsPublicTrueOrderByCreatedAtDesc(Long memberId);
    List<MemberProfileImage> findAllByMemberId(long memberId);
}
