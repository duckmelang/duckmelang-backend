package umc.duckmelang.domain.application.converter;

import org.springframework.data.domain.Page;
import umc.duckmelang.domain.application.domain.Application;
import umc.duckmelang.domain.application.dto.ApplicationResponseDto;
import umc.duckmelang.domain.application.dto.ReceivedApplicationDto;
import umc.duckmelang.domain.application.dto.SentApplicationDto;
import umc.duckmelang.domain.materelationship.domain.MateRelationship;
import java.util.*;
import java.util.stream.Collectors;

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

    public static ApplicationResponseDto.SentApplicationListDto toSentApplicationListDto(Page<SentApplicationDto> applications) {
        List<SentApplicationDto> sentApplicationList = applications.stream().collect(Collectors.toList());
        return ApplicationResponseDto.SentApplicationListDto.builder()
                .sentApplicationList(sentApplicationList)
                .listSize(sentApplicationList.size())
                .totalPage(applications.getTotalPages())
                .totalElements(applications.getTotalElements())
                .isFirst(applications.isFirst())
                .isLast(applications.isLast())
                .build();
    }

    public static SentApplicationDto toSentApplicationDto(Application application) {
        return SentApplicationDto.builder()
                .postId(application.getPost().getId())
                .postTitle(application.getPost().getTitle())
                .postMemberNickname(application.getPost().getMember().getNickname())
                .eventDate(application.getPost().getEventDate())
                .eventCategory(application.getPost().getEventCategory().getName())
                .applicationId(application.getId())
                .applicationStatus(application.getStatus())
                .build();
    }

    public static ApplicationResponseDto.ReceivedApplicationListDto toReceivedApplicationListDto(Page<ReceivedApplicationDto> applications) {
        List<ReceivedApplicationDto> receivedApplicationList = applications.stream().collect(Collectors.toList());
        return ApplicationResponseDto.ReceivedApplicationListDto.builder()
                .receivedApplicationList(receivedApplicationList)
                .listSize(receivedApplicationList.size())
                .totalPage(applications.getTotalPages())
                .totalElements(applications.getTotalElements())
                .isFirst(applications.isFirst())
                .isLast(applications.isLast())
                .build();
    }

    public static ReceivedApplicationDto toReceivedApplicationDto(Application application) {
        return ReceivedApplicationDto.builder()
                .applicationMemberNickname(application.getMember().getNickname())
                .postId(application.getPost().getId())
                .postTitle(application.getPost().getTitle())
                .applicationCreatedAt(application.getCreatedAt())
                .applicationStatus(application.getStatus())
                .build();
    }
}
