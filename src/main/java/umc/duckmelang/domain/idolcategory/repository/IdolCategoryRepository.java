package umc.duckmelang.domain.idolcategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc.duckmelang.domain.idolcategory.domain.IdolCategory;

@Repository
public interface IdolCategoryRepository extends JpaRepository<IdolCategory, Long> {
}
