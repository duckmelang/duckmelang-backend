package umc.duckmelang.domain.idolcategory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.idolcategory.domain.IdolCategory;
import umc.duckmelang.domain.idolcategory.repository.IdolCategoryRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IdolCategoryQueryServiceImpl implements IdolCategoryQueryService {
    private final IdolCategoryRepository idolCategoryRepository;

    @Override
    public Optional<IdolCategory> findIdolCategory(Long id) {
        return idolCategoryRepository.findById(id);
    }
}
