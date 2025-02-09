package umc.duckmelang.domain.idolcategory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.duckmelang.domain.idolcategory.domain.IdolCategory;
import umc.duckmelang.domain.idolcategory.repository.IdolCategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IdolCategoryQueryServiceImpl implements IdolCategoryQueryService {
    private final IdolCategoryRepository idolCategoryRepository;

    @Override
    public Optional<IdolCategory> findIdolCategory(Long id) {
        return idolCategoryRepository.findById(id);
    }

    @Override
    public List<IdolCategory> getIdolListByKeyword(String keyword){
        return idolCategoryRepository.findByKeyword(keyword);
    }
}
