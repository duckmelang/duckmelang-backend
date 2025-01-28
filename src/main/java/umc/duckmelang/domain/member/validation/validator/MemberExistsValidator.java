package umc.duckmelang.domain.member.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import umc.duckmelang.domain.member.service.MemberQueryService;
import umc.duckmelang.domain.member.validation.annotation.ExistsMember;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;

@Component
@RequiredArgsConstructor
public class MemberExistsValidator implements ConstraintValidator<ExistsMember, Long> {
    private final MemberQueryService memberQueryService;

    @Override
    public void initialize(ExistsMember constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long memberId, ConstraintValidatorContext context) {
        boolean isValid = memberQueryService.existsById(memberId);
        if(!isValid){
            context.buildConstraintViolationWithTemplate(ErrorStatus.NO_SUCH_MEMBER.toString()).addConstraintViolation();
        }
        return isValid;
    }
}
