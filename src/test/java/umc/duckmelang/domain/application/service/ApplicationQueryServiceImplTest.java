package umc.duckmelang.domain.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import umc.duckmelang.domain.application.domain.enums.ApplicationStatus;
import umc.duckmelang.domain.application.dto.ReceivedApplicationDto;
import umc.duckmelang.domain.application.dto.SentApplicationDto;
import umc.duckmelang.domain.application.repository.ApplicationRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationQueryServiceImplTest {

    @InjectMocks
    private ApplicationQueryServiceImpl applicationQueryService;

    @Mock
    private ApplicationRepository applicationRepository;

    private Long memberId;
    private Integer page;
    private PageRequest pageRequest;

    @BeforeEach
    void setUp() {
        memberId = 1L;
        page = 0;
        pageRequest = PageRequest.of(page, 10);
    }

    @Test
    @DisplayName("지원서 ID로 유효성 검사")
    void isValid() {
        // given
        Long applicationId = 1L;
        when(applicationRepository.existsById(applicationId)).thenReturn(true);

        // when
        boolean result = applicationQueryService.isValid(applicationId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("받은 승인된 지원서 목록 조회")
    void getReceivedSucceedingApplicationList() {
        // given
        List<ReceivedApplicationDto> dtoList = Arrays.asList(
                new ReceivedApplicationDto("User1", 1L, "Post1", 1L, LocalDateTime.now()),
                new ReceivedApplicationDto("User2", 2L, "Post2", 2L, LocalDateTime.now())
        );
        Page<ReceivedApplicationDto> expectedPage = new PageImpl<>(dtoList);

        when(applicationRepository.findReceivedApplicationList(
                eq(memberId),
                eq(ApplicationStatus.SUCCEED),
                any(PageRequest.class)
        )).thenReturn(expectedPage);

        // when
        Page<ReceivedApplicationDto> result = applicationQueryService.getReceivedSucceedingApplicationList(memberId, page);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).isEqualTo(dtoList);
    }

    @Test
    @DisplayName("받은 거절된 지원서 목록 조회")
    void getReceivedFailedApplicationList() {
        // given
        List<ReceivedApplicationDto> dtoList = Arrays.asList(
                new ReceivedApplicationDto("User3", 3L, "Post3", 3L, LocalDateTime.now()),
                new ReceivedApplicationDto("User4", 4L, "Post4", 4L, LocalDateTime.now())
        );
        Page<ReceivedApplicationDto> expectedPage = new PageImpl<>(dtoList);

        when(applicationRepository.findReceivedApplicationList(
                eq(memberId),
                eq(ApplicationStatus.FAILED),
                any(PageRequest.class)
        )).thenReturn(expectedPage);

        // when
        Page<ReceivedApplicationDto> result = applicationQueryService.getReceivedFailedApplicationList(memberId, page);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).isEqualTo(dtoList);
    }

    @Test
    @DisplayName("받은 대기중인 지원서 목록 조회")
    void getReceivedPendingApplicationList() {
        // given
        List<ReceivedApplicationDto> dtoList = Arrays.asList(
                new ReceivedApplicationDto("User5", 5L, "Post5", 5L, LocalDateTime.now()),
                new ReceivedApplicationDto("User6", 6L, "Post6", 6L, LocalDateTime.now())
        );
        Page<ReceivedApplicationDto> expectedPage = new PageImpl<>(dtoList);

        when(applicationRepository.findReceivedApplicationList(
                eq(memberId),
                eq(ApplicationStatus.PENDING),
                any(PageRequest.class)
        )).thenReturn(expectedPage);

        // when
        Page<ReceivedApplicationDto> result = applicationQueryService.getReceivedPendingApplicationList(memberId, page);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).isEqualTo(dtoList);
    }

    @Test
    @DisplayName("보낸 승인된 지원서 목록 조회")
    void getSentSucceedingApplicationList() {
        // given
        List<SentApplicationDto> dtoList = Arrays.asList(
                new SentApplicationDto(1L, "Post1", "Nickname1", LocalDateTime.now(), "Event1", 1L),
                new SentApplicationDto(2L, "Post2", "Nickname2", LocalDateTime.now(), "Event2", 2L)
        );
        Page<SentApplicationDto> expectedPage = new PageImpl<>(dtoList);

        when(applicationRepository.findSentApplicationList(
                eq(memberId),
                eq(ApplicationStatus.SUCCEED),
                any(PageRequest.class)
        )).thenReturn(expectedPage);

        // when
        Page<SentApplicationDto> result = applicationQueryService.getSentSucceedingApplicationList(memberId, page);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).isEqualTo(dtoList);
    }

    @Test
    @DisplayName("보낸 거절된 지원서 목록 조회")
    void getSentFailedApplicationList() {
        // given
        List<SentApplicationDto> dtoList = Arrays.asList(
                new SentApplicationDto(3L, "Post3", "Nickname3", LocalDateTime.now(), "Event3", 3L),
                new SentApplicationDto(4L, "Post4", "Nickname4", LocalDateTime.now(), "Event4", 4L)
        );
        Page<SentApplicationDto> expectedPage = new PageImpl<>(dtoList);

        when(applicationRepository.findSentApplicationList(
                eq(memberId),
                eq(ApplicationStatus.FAILED),
                any(PageRequest.class)
        )).thenReturn(expectedPage);

        // when
        Page<SentApplicationDto> result = applicationQueryService.getSentFailedApplicationList(memberId, page);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).isEqualTo(dtoList);
    }

    @Test
    @DisplayName("보낸 대기중인 지원서 목록 조회")
    void getSentPendingApplicationList() {
        // given
        List<SentApplicationDto> dtoList = Arrays.asList(
                new SentApplicationDto(5L, "Post5", "Nickname5", LocalDateTime.now(), "Event5", 5L),
                new SentApplicationDto(6L, "Post6", "Nickname6", LocalDateTime.now(), "Event6", 6L)
        );
        Page<SentApplicationDto> expectedPage = new PageImpl<>(dtoList);

        when(applicationRepository.findSentApplicationList(
                eq(memberId),
                eq(ApplicationStatus.PENDING),
                any(PageRequest.class)
        )).thenReturn(expectedPage);

        // when
        Page<SentApplicationDto> result = applicationQueryService.getSentPendingApplicationList(memberId, page);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).isEqualTo(dtoList);
    }
}