package umc.duckmelang.global.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import umc.duckmelang.global.validation.validator.IdolExistValidator;
import umc.duckmelang.global.validation.validator.NotificationExistValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotificationExistValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistNotification {

    String message() default "해당하는 알림이 존재하지 않습니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
