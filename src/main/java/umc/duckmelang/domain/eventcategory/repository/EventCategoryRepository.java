package umc.duckmelang.domain.eventcategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc.duckmelang.domain.eventcategory.domain.EventCategory;

@Repository
public interface EventCategoryRepository extends JpaRepository<EventCategory, Long> {
}
