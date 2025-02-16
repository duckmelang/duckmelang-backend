package umc.duckmelang.domain.landmine.converter;

import org.springframework.stereotype.Component;
import umc.duckmelang.domain.landmine.domain.Landmine;
import umc.duckmelang.domain.landmine.dto.LandmineResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LandmineConverter {

    public static LandmineResponseDto.LandmineListDto toLandmineListDto(List<Landmine> landmines){
        List<LandmineResponseDto.LandmineDto> landmineList = landmines.stream()
                .map(LandmineConverter::toLandmineDto)
                .collect(Collectors.toList());

        return LandmineResponseDto.LandmineListDto.builder()
                .landmineList(landmineList)
                .build();
    }

    public static LandmineResponseDto.LandmineDto toLandmineDto(Landmine landmine){
        return LandmineResponseDto.LandmineDto.builder()
                .landmineId(landmine.getId())
                .content(landmine.getContent())
                .build();
    }
}
