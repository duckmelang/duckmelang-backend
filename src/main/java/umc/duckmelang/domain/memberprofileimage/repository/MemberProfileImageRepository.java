package umc.duckmelang.domain.memberprofileimage.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import java.util.List;
public interface MemberProfileImageRepository extends JpaRepository<MemberProfileImage, Long> {
    @Query("SELECT m.memberImage FROM MemberProfileImage m WHERE m.member.id = :memberId")
    Page<String> findImageUrlsByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}
