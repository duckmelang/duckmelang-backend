package umc.duckmelang.domain.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.duckmelang.domain.application.domain.Application;
import umc.duckmelang.domain.application.domain.enums.ApplicationStatus;
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

    // 대기중을 제외한 수락/거절 상태인 동행요청 조회
    @Override
    public Page<Application> getReceivedApplicationListExceptPending(Long memberId, Integer page) {
        return applicationRepository.findReceivedApplicationListExceptPending(memberId, PageRequest.of(page, 10));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Application> getReceivedSucceedingApplicationList(Long memberId, Integer page) {
        return applicationRepository.findReceivedApplicationListByStatus(memberId, ApplicationStatus.SUCCEED, PageRequest.of(page,10));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Application> getReceivedFailedApplicationList(Long memberId, Integer page) {
        return applicationRepository.findReceivedApplicationListByStatus(memberId, ApplicationStatus.FAILED, PageRequest.of(page,10));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Application> getReceivedPendingApplicationList(Long memberId, Integer page) {
        return applicationRepository.findReceivedApplicationListByStatus(memberId, ApplicationStatus.PENDING, PageRequest.of(page,10));
    }

    @Override
    public Page<Application> getSentApplicationList(Long memberId, Integer page) {
        return applicationRepository.findSentApplicationList(memberId, PageRequest.of(page, 10));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Application> getSentSucceedingApplicationList(Long memberId, Integer page) {
        return applicationRepository.findSentApplicationListByStatus(memberId, ApplicationStatus.SUCCEED, PageRequest.of(page,10));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Application> getSentFailedApplicationList(Long memberId, Integer page) {
        return applicationRepository.findSentApplicationListByStatus(memberId, ApplicationStatus.FAILED, PageRequest.of(page,10));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Application> getSentPendingApplicationList(Long memberId, Integer page) {
        return applicationRepository.findSentApplicationListByStatus(memberId, ApplicationStatus.PENDING, PageRequest.of(page,10));
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
