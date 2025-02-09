package umc.duckmelang.domain.idolcategory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class IdolCategoryResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IdolDto {
        private Long idolId;
        private String idolName;
        private String idolImage;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IdolListDto {
        List<IdolDto> idolList;
    }
}
