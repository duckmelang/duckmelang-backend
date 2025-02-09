package umc.duckmelang.domain.landmine.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.landmine.converter.LandmineConverter;
import umc.duckmelang.domain.landmine.domain.Landmine;
import umc.duckmelang.domain.landmine.dto.LandmineResponseDto;
import umc.duckmelang.domain.landmine.repository.LandmineRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LandmineQueryServiceImpl implements LandmineQueryService{
    private final LandmineRepository landmineRepository;
    private final LandmineConverter landmineConverter;

    @Override
    public LandmineResponseDto.LandmineListDto getLandmineList(Long memberId){
        List<Landmine> landmineList = landmineRepository.findByMemberId(memberId);
        return landmineConverter.toLandmineListDto(landmineList);
    }
}
