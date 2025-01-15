package umc.duckmelang.domain.application.converter;

import umc.duckmelang.domain.application.domain.Application;
import umc.duckmelang.domain.application.dto.ApplicationResponseDto;
import umc.duckmelang.domain.materelationship.domain.MateRelationship;

public class ApplicationConverter {
    public static ApplicationResponseDto.ApplicationStatusChangeResponseDto toApplicationStatusChangeResponseDto(Application application) {
        return ApplicationResponseDto.ApplicationStatusChangeResponseDto.builder()
                .newStatus(application.getStatus())
                .modifiedAt(application.getUpdatedAt())
                .build();
    }

    public static ApplicationResponseDto.MateRelationshipCreateResponseDto toMateRelationshipCreateResponseDto(MateRelationship mateRelationship) {
        return ApplicationResponseDto.MateRelationshipCreateResponseDto.builder()
                .mateRelationshipId(mateRelationship.getId())
                .createdAt(mateRelationship.getCreatedAt())
                .build();
    }
}
