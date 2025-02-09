package umc.duckmelang.domain.landmine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc.duckmelang.domain.landmine.domain.Landmine;
import umc.duckmelang.domain.member.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface LandmineRepository extends JpaRepository<Landmine, Long> {
    void deleteAllByMember(Member member);
    List<Landmine> findByMemberId(Long memberId);
    Optional<Landmine> findByIdAndMemberId(Long landmineId, Long memberId);
}
