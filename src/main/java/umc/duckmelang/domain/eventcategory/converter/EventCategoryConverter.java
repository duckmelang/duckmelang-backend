package umc.duckmelang.domain.eventcategory.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import umc.duckmelang.domain.eventcategory.domain.EventCategory;
import umc.duckmelang.domain.eventcategory.dto.EventCategoryResponseDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventCategoryConverter {

    public List<EventCategoryResponseDto.EventCategoryDto> groupCategoriesByKind(List<EventCategory> eventCategories){
        Map<String, List<String>> groupedCategories = eventCategories.stream()
                .collect(Collectors.groupingBy(
                        category -> category.getKind().getName(),
                        Collectors.mapping(EventCategory::getName, Collectors.toList())
                ));

        return groupedCategories.entrySet().stream()
                .map(entry -> EventCategoryResponseDto.EventCategoryDto.builder()
                        .kind(entry.getKey())
                        .eventCategories(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }
}
