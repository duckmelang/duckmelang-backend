package umc.duckmelang.domain.member.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import umc.duckmelang.domain.application.validation.validator.ApplicationExistsValidator;
import umc.duckmelang.domain.member.validation.validator.MemberExistsValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MemberExistsValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsMember {
    String message() default "존재하지 않는 회원입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
