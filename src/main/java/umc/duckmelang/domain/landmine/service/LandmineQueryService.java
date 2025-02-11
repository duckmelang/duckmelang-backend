package umc.duckmelang.domain.landmine.service;

import umc.duckmelang.domain.landmine.dto.LandmineResponseDto;

public interface LandmineQueryService {
    LandmineResponseDto.LandmineListDto getLandmineList(Long memberId);
}
