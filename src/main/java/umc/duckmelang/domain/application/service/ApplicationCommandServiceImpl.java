package umc.duckmelang.domain.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.duckmelang.domain.application.domain.Application;
import umc.duckmelang.domain.application.domain.enums.ApplicationStatus;
import umc.duckmelang.domain.application.repository.ApplicationRepository;
import umc.duckmelang.domain.materelationship.domain.MateRelationship;
import umc.duckmelang.domain.materelationship.repository.MateRelationshipRepository;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.handler.ApplicationHandler;

@Service
@RequiredArgsConstructor
public class ApplicationCommandServiceImpl implements ApplicationCommandService{
    private final ApplicationRepository applicationRepository;
    private final MateRelationshipRepository mateRelationshipRepository;

    @Override
    @Transactional
    public Application updateStatusToFailed(Long applicationId, Long memberId) {
        // TO-DO : authorize JWT

        Application application = applicationRepository
                .findByIdAndPostMemberId(applicationId, memberId)
                .orElseThrow(() -> new ApplicationHandler(ErrorStatus.APPLICATION_NOT_FOUND));

        if (!application.updateStatus(ApplicationStatus.FAILED)) {
            throw new ApplicationHandler(ErrorStatus.ALREADY_PROCESSED_APPLICATION);
        }

        applicationRepository.save(application);
        return application;
    }

    @Override
    @Transactional
    public Application updateStatusToCanceled(Long applicationId, Long memberId) {
        // TO-DO : authorize JWT

        Application application = applicationRepository
                .findByIdAndMemberId(applicationId, memberId)
                .orElseThrow(() -> new ApplicationHandler(ErrorStatus.APPLICATION_NOT_FOUND));

        if (!application.updateStatus(ApplicationStatus.CANCELED)) {
            throw new ApplicationHandler(ErrorStatus.ALREADY_PROCESSED_APPLICATION);
        }

        applicationRepository.save(application);
        return application;
    }

    @Override
    @Transactional
    public MateRelationship updateStatusToSucceed(Long applicationId, Long memberId) {
        // TO-DO : authorize JWT

        Application application = applicationRepository
                .findByIdAndPostMemberId(applicationId, memberId)
                .orElseThrow(() -> new ApplicationHandler(ErrorStatus.APPLICATION_NOT_FOUND));

        if (!application.updateStatus(ApplicationStatus.SUCCEED)) {
            throw new ApplicationHandler(ErrorStatus.ALREADY_PROCESSED_APPLICATION);
        }

        MateRelationship newRelationship = MateRelationship.builder()
                .firstMember(application.getPost().getMember())
                .secondMember(application.getMember())
                .application(application).build();

        application.setMateRelationship(newRelationship);

        mateRelationshipRepository.save(newRelationship);
        applicationRepository.save(application);

        return newRelationship;
    }

}
