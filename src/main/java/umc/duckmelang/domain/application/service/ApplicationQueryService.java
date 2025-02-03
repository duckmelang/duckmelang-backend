package umc.duckmelang.domain.application.service;

import org.springframework.data.domain.Page;
import umc.duckmelang.domain.application.domain.Application;

public interface ApplicationQueryService {
    boolean isValid(Long id);

    // 받은 동행요청 조회

    // 대기중을 제외한 수락/거절 상태인 동행요청 조회
    Page<Application> getReceivedApplicationListExceptPending(Long memberId, Integer page);
    Page<Application> getReceivedSucceedingApplicationList(Long memberId, Integer page);
    Page<Application> getReceivedFailedApplicationList(Long memberId, Integer page);
    Page<Application> getReceivedPendingApplicationList(Long memberId, Integer page);

    //보낸 동행요청 조회
    Page<Application> getSentApplicationList(Long memberId, Integer page);
    Page<Application> getSentSucceedingApplicationList(Long memberId, Integer page);
    Page<Application> getSentFailedApplicationList(Long memberId, Integer page);
    Page<Application> getSentPendingApplicationList(Long memberId, Integer page);

    // 멤버 id를 받아 1. post 작성자거나 2. application 작성자고, 3. succeed한 application 개수 조회
    int countMatchedApplications(Long memberId);
}
