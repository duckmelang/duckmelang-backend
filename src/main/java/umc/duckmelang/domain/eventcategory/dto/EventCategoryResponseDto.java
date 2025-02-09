package umc.duckmelang.domain.eventcategory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


public class EventCategoryResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EventCategoryDto{
        private String kind;
        private List<String> eventCategories;
    }
}
