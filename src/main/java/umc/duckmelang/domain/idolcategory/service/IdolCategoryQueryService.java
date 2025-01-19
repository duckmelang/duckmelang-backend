package umc.duckmelang.domain.idolcategory.service;

import umc.duckmelang.domain.idolcategory.domain.IdolCategory;

import java.util.Optional;

public interface IdolCategoryQueryService {

    Optional<IdolCategory> findIdolCategory(Long id);
}
