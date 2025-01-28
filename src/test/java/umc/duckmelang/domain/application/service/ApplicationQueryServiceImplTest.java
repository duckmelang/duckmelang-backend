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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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
    @DisplayName("받은 대기중인 지원서 목록 조회")
    void getReceivedPendingApplicationList() {
        // given
        LocalDateTime now = LocalDateTime.now();
        List<ReceivedApplicationDto> dtoList = Arrays.asList(
                ReceivedApplicationDto.builder()
                        .applicationMemberNickname("user1")
                        .postId(1L)
                        .postTitle("title1")
                        .applicationId(1L)
                        .applicationCreatedAt(now)
                        .applicationStatus(ApplicationStatus.SUCCEED)
                        .build(),
                ReceivedApplicationDto.builder()
                        .applicationMemberNickname("user2")
                        .postId(2L)
                        .postTitle("title2")
                        .applicationId(2L)
                        .applicationCreatedAt(now.plusMinutes(1))
                        .applicationStatus(ApplicationStatus.SUCCEED)
                        .build(),
                ReceivedApplicationDto.builder()
                        .applicationMemberNickname("user3")
                        .postId(3L)
                        .postTitle("title3")
                        .applicationId(3L)
                        .applicationCreatedAt(now.plusMinutes(2))
                        .applicationStatus(ApplicationStatus.PENDING)
                        .build(),
                ReceivedApplicationDto.builder()
                        .applicationMemberNickname("user4")
                        .postId(4L)
                        .postTitle("title4")
                        .applicationId(4L)
                        .applicationCreatedAt(now.plusMinutes(3))
                        .applicationStatus(ApplicationStatus.PENDING)
                        .build()
        );
        List<ReceivedApplicationDto> pendingList = dtoList.stream()
                .filter(dto -> dto.getApplicationStatus() == ApplicationStatus.PENDING)
                .collect(Collectors.toList());
        Page<ReceivedApplicationDto> expectedPage = new PageImpl<>(pendingList);

        when(applicationRepository.findReceivedApplicationListByStatus(eq(memberId), eq(ApplicationStatus.PENDING), any(PageRequest.class)))
                .thenReturn(expectedPage);

        // when
        Page<ReceivedApplicationDto> result = applicationQueryService.getReceivedPendingApplicationList(memberId, page);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent())
                .extracting("applicationStatus")
                .containsOnly(ApplicationStatus.PENDING);
        assertThat(result.getContent())
                .extracting("applicationMemberNickname")
                .containsExactly("user3", "user4");
        verify(applicationRepository, times(1)).findReceivedApplicationListByStatus(eq(memberId), eq(ApplicationStatus.PENDING), any(PageRequest.class));
    }
    @Test
    @DisplayName("받은 수락/거절 지원서 목록 조회")
    void getReceivedApplicationListExceptPending() {
        // given
        LocalDateTime now = LocalDateTime.now();
        List<ReceivedApplicationDto> dtoList = Arrays.asList(
                ReceivedApplicationDto.builder()
                        .applicationMemberNickname("user1")
                        .postId(1L)
                        .postTitle("title1")
                        .applicationId(1L)
                        .applicationCreatedAt(now)
                        .applicationStatus(ApplicationStatus.SUCCEED)
                        .build(),
                ReceivedApplicationDto.builder()
                        .applicationMemberNickname("user2")
                        .postId(2L)
                        .postTitle("title2")
                        .applicationId(2L)
                        .applicationCreatedAt(now.plusMinutes(1))
                        .applicationStatus(ApplicationStatus.SUCCEED)
                        .build(),
                ReceivedApplicationDto.builder()
                        .applicationMemberNickname("user5")
                        .postId(5L)
                        .postTitle("title5")
                        .applicationId(5L)
                        .applicationCreatedAt(now.plusMinutes(4))
                        .applicationStatus(ApplicationStatus.FAILED)
                        .build(),
                ReceivedApplicationDto.builder()
                        .applicationMemberNickname("user6")
                        .postId(6L)
                        .postTitle("title6")
                        .applicationId(6L)
                        .applicationCreatedAt(now.plusMinutes(5))
                        .applicationStatus(ApplicationStatus.FAILED)
                        .build()
        );

        List<ReceivedApplicationDto> nonPendingList = dtoList.stream()
                .filter(dto -> dto.getApplicationStatus() != ApplicationStatus.PENDING)
                .collect(Collectors.toList());
        Page<ReceivedApplicationDto> expectedPage = new PageImpl<>(nonPendingList);

        when(applicationRepository.findReceivedApplicationListExceptPending(eq(memberId), any(PageRequest.class)))
                .thenReturn(expectedPage);

        // when
        Page<ReceivedApplicationDto> result = applicationQueryService.getReceivedApplicationListExceptPending(memberId, page);

        // then
        assertThat(result.getContent()).hasSize(4);
        assertThat(result.getContent())
                .extracting("applicationStatus")
                .containsOnly(ApplicationStatus.SUCCEED, ApplicationStatus.FAILED);
        assertThat(result.getContent())
                .extracting("applicationMemberNickname")
                .containsExactly("user1", "user2", "user5", "user6");
        verify(applicationRepository, times(1)).findReceivedApplicationListExceptPending(eq(memberId), any(PageRequest.class));
    }


    @Test
    @DisplayName("보낸 지원서 목록 조회")
    void getSentPendingApplicationList() {
        // given
        List<SentApplicationDto> dtoList = Arrays.asList(
                SentApplicationDto.builder()
                        .postId(1L)
                        .postTitle("Post1")
                        .postMemberNickname("Nickname1")
                        .eventDate(LocalDate.now())
                        .eventCategory("Event1")
                        .applicationId(1L)
                        .applicationStatus(ApplicationStatus.PENDING)
                        .build(),
                SentApplicationDto.builder()
                        .postId(2L)
                        .postTitle("Post2")
                        .postMemberNickname("Nickname2")
                        .eventDate(LocalDate.now())
                        .eventCategory("Event2")
                        .applicationId(2L)
                        .applicationStatus(ApplicationStatus.PENDING)
                        .build(),
                SentApplicationDto.builder()
                        .postId(3L)
                        .postTitle("Post3")
                        .postMemberNickname("Nickname3")
                        .eventDate(LocalDate.now())
                        .eventCategory("Event3")
                        .applicationId(3L)
                        .applicationStatus(ApplicationStatus.SUCCEED)
                        .build(),
                SentApplicationDto.builder()
                        .postId(4L)
                        .postTitle("Post4")
                        .postMemberNickname("Nickname4")
                        .eventDate(LocalDate.now())
                        .eventCategory("Event4")
                        .applicationId(4L)
                        .applicationStatus(ApplicationStatus.SUCCEED)
                        .build(),
                SentApplicationDto.builder()
                        .postId(5L)
                        .postTitle("Post5")
                        .postMemberNickname("Nickname5")
                        .eventDate(LocalDate.now())
                        .eventCategory("Event5")
                        .applicationId(5L)
                        .applicationStatus(ApplicationStatus.FAILED)
                        .build(),
                SentApplicationDto.builder()
                        .postId(6L)
                        .postTitle("Post6")
                        .postMemberNickname("Nickname6")
                        .eventDate(LocalDate.now())
                        .eventCategory("Event6")
                        .applicationId(6L)
                        .applicationStatus(ApplicationStatus.FAILED)
                        .build()
        );
        Page<SentApplicationDto> expectedPage = new PageImpl<>(dtoList);

        when(applicationRepository.findSentApplicationList(
                eq(memberId),
                any(PageRequest.class)
        )).thenReturn(expectedPage);

        // when
        Page<SentApplicationDto> result = applicationQueryService.getSentApplicationList(memberId, page);

        // then
        assertThat(result.getContent()).hasSize(6);
        assertThat(result.getContent()).isEqualTo(dtoList);
    }
}