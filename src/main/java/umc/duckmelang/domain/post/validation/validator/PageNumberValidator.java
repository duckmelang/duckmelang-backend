package umc.duckmelang.domain.post.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import umc.duckmelang.domain.post.validation.annotation.ValidPageNumber;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;


@Component
@RequiredArgsConstructor
public class PageNumberValidator implements ConstraintValidator<ValidPageNumber, Integer> {
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null || value < 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.PAGE_MUST_LEAST_ZERO.toString())
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

}
