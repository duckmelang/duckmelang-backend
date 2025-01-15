package umc.duckmelang.domain.idolcategory.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import umc.duckmelang.domain.idolcategory.domain.IdolCategory;
import umc.duckmelang.domain.idolcategory.repository.IdolCategoryRepository;
import umc.duckmelang.domain.idolcategory.validation.annotation.ExistIdol;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;

import java.util.List;

@Component
@RequiredArgsConstructor
public class IdolExistValidator implements ConstraintValidator<ExistIdol, Long> {

    private final IdolCategoryRepository idolCategoryRepository;

    @Override
    public void initialize(ExistIdol constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long idolId, ConstraintValidatorContext context) {
        boolean isValid = idolCategoryRepository.existsById(idolId);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.IDOL_CATEGORY_NOT_FOUND.toString()).addConstraintViolation();
        }

        return isValid;
    }
}
