package umc.duckmelang.global.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import umc.duckmelang.global.validation.validator.ApplicationExistsValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ApplicationExistsValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsApplication {
    String message() default "존재하지 않는 동행 요청입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
