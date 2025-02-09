package umc.duckmelang.domain.idolcategory.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import umc.duckmelang.domain.idolcategory.domain.IdolCategory;
import umc.duckmelang.domain.idolcategory.dto.IdolCategoryResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class IdolCategoryConverter {

    public static IdolCategoryResponseDto.IdolDto toIdolDto(IdolCategory idolCategory){
        return IdolCategoryResponseDto.IdolDto.builder()
                .idolId(idolCategory.getId())
                .idolName(idolCategory.getName())
                .idolImage(idolCategory.getProfileImage())
                .build();
    }

    public static IdolCategoryResponseDto.IdolListDto toIdolListDto(List<IdolCategory> idolCategoryList){
        List<IdolCategoryResponseDto.IdolDto> idolDtoList = idolCategoryList.stream()
                .map(IdolCategoryConverter::toIdolDto).collect(Collectors.toList());

        return IdolCategoryResponseDto.IdolListDto.builder()
                .idolList(idolDtoList)
                .build();
    }
}
