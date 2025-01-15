package umc.duckmelang.domain.application.service;

import umc.duckmelang.domain.application.domain.Application;
import umc.duckmelang.domain.materelationship.domain.MateRelationship;

public interface ApplicationCommandService {
    Application updateStatusToFailed(Long applicationId, Long memberId);
    Application updateStatusToCanceled(Long applicationId, Long memberId);
    MateRelationship updateStatusToSucceed(Long applicationId, Long memberId);
}
