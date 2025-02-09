package umc.duckmelang.domain.landmine.service;

import umc.duckmelang.domain.landmine.domain.Landmine;

public interface LandmineCommandService {
    Landmine addLandmine(Long memberId, String content);
    void removeLandmine(Long memberId, Long landmineId);
}
