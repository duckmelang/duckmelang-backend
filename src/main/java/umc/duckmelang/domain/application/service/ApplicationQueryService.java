package umc.duckmelang.domain.application.service;

import org.springframework.data.domain.Page;
import umc.duckmelang.domain.application.dto.ReceivedApplicationDto;
import umc.duckmelang.domain.application.dto.SentApplicationDto;

public interface ApplicationQueryService {
    boolean isValid(Long id);

    // 받은 동행요청 조회

    // 대기중을 제외한 수락/거절 상태인 동행요청 조회
    Page<ReceivedApplicationDto> getReceivedApplicationListExceptPending(Long memberId, Integer page);
    Page<ReceivedApplicationDto> getReceivedSucceedingApplicationList(Long memberId, Integer page);
    Page<ReceivedApplicationDto> getReceivedFailedApplicationList(Long memberId, Integer page);
    Page<ReceivedApplicationDto> getReceivedPendingApplicationList(Long memberId, Integer page);

    //보낸 동행요청 조회
    Page<SentApplicationDto> getSentApplicationList(Long memberId, Integer page);
    Page<SentApplicationDto> getSentSucceedingApplicationList(Long memberId, Integer page);
    Page<SentApplicationDto> getSentFailedApplicationList(Long memberId, Integer page);
    Page<SentApplicationDto> getSentPendingApplicationList(Long memberId, Integer page);

    // 멤버 id를 받아 1. post 작성자거나 2. application 작성자고, 3. succeed한 application 개수 조회
    int countMatchedApplications(Long memberId);
}
