package umc.duckmelang.domain.member.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import umc.duckmelang.domain.member.validation.validator.MemberExistValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MemberExistValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistMember {
    String message() default "해당하는 Id의 member가 없습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}