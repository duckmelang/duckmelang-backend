package umc.duckmelang.domain.landmine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc.duckmelang.domain.landmine.domain.Landmine;
import umc.duckmelang.domain.member.domain.Member;

@Repository
public interface LandmineRepository extends JpaRepository<Landmine, Long> {
    void deleteAllByMember(Member member);
}
