package umc.duckmelang.domain.eventcategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.duckmelang.domain.eventcategory.domain.EventCategory;

public interface EventCategoryRepository extends JpaRepository<EventCategory, Long> {
}
