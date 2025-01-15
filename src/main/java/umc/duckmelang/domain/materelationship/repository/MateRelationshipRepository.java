package umc.duckmelang.domain.materelationship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.duckmelang.domain.materelationship.domain.MateRelationship;

public interface MateRelationshipRepository extends JpaRepository<MateRelationship, Long> {
}
