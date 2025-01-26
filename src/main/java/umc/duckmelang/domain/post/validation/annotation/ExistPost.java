package umc.duckmelang.domain.post.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import umc.duckmelang.domain.post.validation.validator.PostExistValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PostExistValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistPost {
    String message() default "해당하는 게시글이 없습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
