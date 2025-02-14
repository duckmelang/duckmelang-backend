package umc.duckmelang.domain.notificationsetting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import umc.duckmelang.domain.notificationsetting.domain.NotificationSetting;

import java.util.Optional;

@Repository
public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, Long> {

    @Query("SELECT n from NotificationSetting n JOIN FETCH n.member m where m.id = :memberId")
    Optional<NotificationSetting> findByMemberId(Long memberId);
}
