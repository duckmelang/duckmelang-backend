package umc.duckmelang.domain.application.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import umc.duckmelang.domain.application.service.ApplicationQueryService;
import umc.duckmelang.domain.application.validation.annotation.ExistApplication;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;

@Component
@RequiredArgsConstructor
public class ApplicationExistValidator implements ConstraintValidator<ExistApplication, Long> {
    private final ApplicationQueryService applicationQueryService;
    @Override
    public void initialize(ExistApplication constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long applicationId, ConstraintValidatorContext context) {
        boolean isValid = applicationQueryService.isValid(applicationId);
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.APPLICATION_NOT_FOUND.toString()).addConstraintViolation();
        }
        return isValid;
    }
}
