package umc.duckmelang.domain.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.duckmelang.domain.application.domain.enums.ApplicationStatus;
import umc.duckmelang.domain.application.dto.ReceivedApplicationDto;
import umc.duckmelang.domain.application.dto.SentApplicationDto;
import umc.duckmelang.domain.application.repository.ApplicationRepository;

@Service
@RequiredArgsConstructor
public class ApplicationQueryServiceImpl implements ApplicationQueryService {
    private final ApplicationRepository applicationRepository;

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(Long id) {
        return applicationRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReceivedApplicationDto> getReceivedSucceedingApplicationList(Long memberId, Integer page) {
        return applicationRepository.findReceivedApplicationList(memberId, ApplicationStatus.SUCCEED, PageRequest.of(page,10));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReceivedApplicationDto> getReceivedFailedApplicationList(Long memberId, Integer page) {
        return applicationRepository.findReceivedApplicationList(memberId, ApplicationStatus.FAILED, PageRequest.of(page,10));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReceivedApplicationDto> getReceivedPendingApplicationList(Long memberId, Integer page) {
        return applicationRepository.findReceivedApplicationList(memberId, ApplicationStatus.PENDING, PageRequest.of(page,10));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SentApplicationDto> getSentSucceedingApplicationList(Long memberId, Integer page) {
        return applicationRepository.findSentApplicationList(memberId, ApplicationStatus.SUCCEED, PageRequest.of(page,10));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SentApplicationDto> getSentFailedApplicationList(Long memberId, Integer page) {
        return applicationRepository.findSentApplicationList(memberId, ApplicationStatus.FAILED, PageRequest.of(page,10));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SentApplicationDto> getSentPendingApplicationList(Long memberId, Integer page) {
        return applicationRepository.findSentApplicationList(memberId, ApplicationStatus.PENDING, PageRequest.of(page,10));
    }


    /**
     * 특정 회원의 SUCCEED 상태인 Application 수 조회
     *
     * @param memberId 회원 ID
     * @return SUCCEED 상태인 Application 수(long)
     */
    @Override
    @Transactional(readOnly = true)
    public int countMatchedApplications(Long memberId) {
        return applicationRepository.countByMemberIdOrPostMemberIdAndStatus(memberId, ApplicationStatus.SUCCEED);
    }

}
