package umc.duckmelang.global.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import umc.duckmelang.global.validation.validator.IdolExistValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IdolExistValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistIdol {

    String message() default "해당하는 아이돌이 존재하지 않습니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
