package umc.duckmelang.domain.notification.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.notification.domain.Notification;
import umc.duckmelang.domain.notification.domain.enums.NotificationType;
import umc.duckmelang.domain.notification.dto.NotificationResponseDto;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.dto.PostResponseDto;
import umc.duckmelang.domain.review.converter.ReviewConverter;
import umc.duckmelang.domain.review.dto.ReviewResponseDto;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationConverter {

    public static NotificationResponseDto.NotificationDto notificationDto(Notification notification) {
        return NotificationResponseDto.NotificationDto.builder()
                .id(notification.getId())
                .content(notification.getContent())
                .isRead(notification.getIsRead())
//                .isDelivered(notification.getIsDelivered())
                .type(notification.getNotificationType())
//                .senderNickname(notification.getSender().getNickname())
                .extraData(notification.getExtraData())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    public static NotificationResponseDto.NotificationListDto notificationListDto(List<Notification> notificationList){
        List<NotificationResponseDto.NotificationDto> notificationDtoList = notificationList.stream()
                .map(NotificationConverter::notificationDto).toList();

        return NotificationResponseDto.NotificationListDto.builder()
                .notificationList(notificationDtoList)
                .build();
    }

    public static Notification toNotification(Member sender, Member receiver, NotificationType notificationType, String content, String extraData) {
        return Notification.builder()
                .sender(sender)
                .receiver(receiver)
                .notificationType(notificationType)
                .content(content)
                .isRead(false)
                .extraData(extraData)
                .build();
    }


}
