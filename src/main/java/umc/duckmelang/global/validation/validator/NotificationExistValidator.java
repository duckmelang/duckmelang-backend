package umc.duckmelang.global.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import umc.duckmelang.domain.idolcategory.domain.IdolCategory;
import umc.duckmelang.domain.notification.domain.Notification;
import umc.duckmelang.domain.notification.service.NotificationQueryService;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.validation.annotation.ExistNotification;
import umc.duckmelang.global.validation.annotation.ExistsMember;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NotificationExistValidator implements ConstraintValidator<ExistNotification, Long> {

    private final NotificationQueryService notificationQueryService;

    @Override
    public void initialize(ExistNotification constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long notificationId, ConstraintValidatorContext context) {
        boolean exists = notificationQueryService.existsById(notificationId);

        if (!exists) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.NOTIFICATION_NOT_FOUND.toString()).addConstraintViolation();
            return false;
        }
        return true;
    }
}
