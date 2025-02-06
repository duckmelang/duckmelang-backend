package umc.duckmelang.domain.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.duckmelang.domain.notification.domain.Notification;
import umc.duckmelang.domain.notification.repository.NotificationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationQueryServiceImpl implements NotificationQueryService {
    private  final NotificationRepository notificationRepository;

    @Override
    public List<Notification> getNotificationList(Long memberId){
        return notificationRepository.findAllByReceiverId(memberId);

    }
}
