package umc.duckmelang.global.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import umc.duckmelang.domain.idolcategory.domain.IdolCategory;
import umc.duckmelang.domain.idolcategory.service.IdolCategoryQueryService;
import umc.duckmelang.global.validation.annotation.ExistIdol;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IdolExistValidator implements ConstraintValidator<ExistIdol, Long> {

    private final IdolCategoryQueryService idolCategoryQueryService;

    @Override
    public void initialize(ExistIdol constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long idolId, ConstraintValidatorContext context) {
        Optional<IdolCategory> target = idolCategoryQueryService.findIdolCategory(idolId);

        if (target.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.IDOL_CATEGORY_NOT_FOUND.toString()).addConstraintViolation();
            return false;
        }

        return true;
    }
}
