package umc.duckmelang.domain.idolcategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.duckmelang.domain.idolcategory.domain.IdolCategory;

public interface IdolCategoryRepository extends JpaRepository<IdolCategory, Long> {
}
