package umc.duckmelang.domain.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import umc.duckmelang.domain.application.domain.Application;
import umc.duckmelang.domain.application.domain.enums.ApplicationStatus;
import umc.duckmelang.domain.application.repository.ApplicationRepository;
import umc.duckmelang.domain.materelationship.domain.MateRelationship;
import umc.duckmelang.domain.materelationship.repository.MateRelationshipRepository;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.global.apipayload.exception.ApplicationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationCommandServiceImplTest {

    @InjectMocks
    private ApplicationCommandServiceImpl applicationCommandService;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private MateRelationshipRepository mateRelationshipRepository;

    private Member postOwner;
    private Member applicant;
    private Post post;
    private Application pendingApplication;

    @BeforeEach
    void setUp() {
        // Test data setup with proper relationships
        postOwner = Member.builder()
                .id(1L)
                .name("Post Owner")
                .birth(LocalDate.now())
                .gender(true)
                .email("postowner@test.com")
                .password("password")
                .postList(new ArrayList<>())
                .mateRelationshipinFirstList(new ArrayList<>())
                .build();

        applicant = Member.builder()
                .id(2L)
                .name("Applicant")
                .birth(LocalDate.now())
                .gender(false)
                .email("applicant@test.com")
                .password("password")
                .applicationList(new ArrayList<>())
                .mateRelationshipinSecondList(new ArrayList<>())
                .build();

        post = Post.builder()
                .id(1L)
                .member(postOwner)
                .applicationList(new ArrayList<>())
                .build();
        postOwner.getPostList().add(post);

        pendingApplication = Application.builder()
                .id(1L)
                .status(ApplicationStatus.PENDING)
                .post(post)
                .member(applicant)
                .build();
        post.getApplicationList().add(pendingApplication);
        applicant.getApplicationList().add(pendingApplication);
    }

    @Nested
    @DisplayName("updateStatusToFailed 메소드는")
    class UpdateStatusToFailed {

        @Test
        @DisplayName("PENDING 상태의 신청을 FAILED로 변경할 수 있다")
        void shouldUpdatePendingStatusToFailed() {
            // Given
            when(applicationRepository.findByIdAndPostMemberId(1L, 1L))
                    .thenReturn(Optional.of(pendingApplication));
            when(applicationRepository.save(any(Application.class)))
                    .thenReturn(pendingApplication);

            // When
            Application result = applicationCommandService.updateStatusToFailed(1L, 1L);

            // Then
            assertThat(result.getStatus()).isEqualTo(ApplicationStatus.FAILED);
            assertThat(result.getPost().getMember()).isEqualTo(postOwner);
            assertThat(result.getMember()).isEqualTo(applicant);
            verify(applicationRepository).save(any(Application.class));
        }

        @Test
        @DisplayName("존재하지 않는 신청에 대해 예외를 발생시킨다")
        void shouldThrowExceptionForNonExistentApplication() {
            // Given
            when(applicationRepository.findByIdAndPostMemberId(999L, 1L))
                    .thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> applicationCommandService.updateStatusToFailed(999L, 1L))
                    .isInstanceOf(ApplicationException.class)
                    .matches(ex -> ((ApplicationException) ex).getReason().getCode().equals("APPLICATION4003"));

            verify(applicationRepository, never()).save(any(Application.class));
        }

        @Test
        @DisplayName("이미 처리된 신청은 상태를 변경할 수 없다")
        void shouldNotUpdateAlreadyProcessedApplication() {
            // Given
            Application processedApplication = Application.builder()
                    .id(1L)
                    .status(ApplicationStatus.SUCCEED)  // 이미 SUCCEED 상태로 처리된 신청
                    .post(post)
                    .member(applicant)
                    .build();

            when(applicationRepository.findByIdAndPostMemberId(1L, 1L))
                    .thenReturn(Optional.of(processedApplication));

            // When & Then
            assertThatThrownBy(() -> applicationCommandService.updateStatusToFailed(1L, 1L))
                    .isInstanceOf(ApplicationException.class)
                    .matches(
                            ex -> {
                                ApplicationException appEx = (ApplicationException) ex;
                                System.out.println("Error code: " + appEx.getReason());
                                return appEx.getReason() != null &&
                                        appEx.getReason().getCode() != null &&
                                        appEx.getReason().getCode().equals("APPLICATION4001");
                            },
                            "should have error code APPLICATION4001"
                    );

            verify(applicationRepository, never()).save(any(Application.class));
            assertThat(processedApplication.getStatus()).isEqualTo(ApplicationStatus.SUCCEED);  // 상태가 변경되지 않았는지 확인
        }
    }

    @Nested
    @DisplayName("updateStatusToCanceled 메소드는")
    class UpdateStatusToCanceled {

        @Test
        @DisplayName("PENDING 상태의 신청을 CANCELED로 변경할 수 있다")
        void shouldUpdatePendingStatusToCanceled() {
            // Given
            when(applicationRepository.findByIdAndMemberId(1L, 2L))
                    .thenReturn(Optional.of(pendingApplication));
            when(applicationRepository.save(any(Application.class)))
                    .thenReturn(pendingApplication);

            // When
            Application result = applicationCommandService.updateStatusToCanceled(1L, 2L);

            // Then
            assertThat(result.getStatus()).isEqualTo(ApplicationStatus.CANCELED);
            assertThat(result.getMember().getId()).isEqualTo(applicant.getId());
            verify(applicationRepository).save(any(Application.class));
        }

        @Test
        @DisplayName("다른 사용자의 신청은 취소할 수 없다")
        void shouldNotCancelOtherUserApplication() {
            // Given
            when(applicationRepository.findByIdAndMemberId(1L, 999L))
                    .thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> applicationCommandService.updateStatusToCanceled(1L, 999L))
                    .isInstanceOf(ApplicationException.class)
                    .matches(ex -> ((ApplicationException) ex).getReason().getCode().equals("APPLICATION4003"));

            verify(applicationRepository, never()).save(any(Application.class));
        }
    }

    @Nested
    @DisplayName("updateStatusToSucceed 메소드는")
    class UpdateStatusToSucceed {

        @Test
        @DisplayName("PENDING 상태의 신청을 SUCCEED로 변경하고 MateRelationship을 생성한다")
        void shouldUpdateStatusToSucceedAndCreateMateRelationship() {
            // Given
            when(applicationRepository.findByIdAndPostMemberId(1L, 1L))
                    .thenReturn(Optional.of(pendingApplication));
            when(mateRelationshipRepository.save(any(MateRelationship.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));
            when(applicationRepository.save(any(Application.class)))
                    .thenReturn(pendingApplication);

            // When
            MateRelationship result = applicationCommandService.updateStatusToSucceed(1L, 1L);

            // Then
            assertThat(pendingApplication.getStatus()).isEqualTo(ApplicationStatus.SUCCEED);
            assertThat(result.getFirstMember()).isEqualTo(postOwner);
            assertThat(result.getSecondMember()).isEqualTo(applicant);
            assertThat(result.getApplication()).isEqualTo(pendingApplication);
            assertThat(pendingApplication.getMateRelationship()).isEqualTo(result);

            verify(mateRelationshipRepository).save(any(MateRelationship.class));
            verify(applicationRepository).save(any(Application.class));
        }

        @Test
        @DisplayName("게시글 작성자가 아닌 사용자는 신청을 수락할 수 없다")
        void shouldNotAllowNonOwnerToAcceptApplication() {
            // Given
            when(applicationRepository.findByIdAndPostMemberId(1L, 999L))
                    .thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> applicationCommandService.updateStatusToSucceed(1L, 999L))
                    .isInstanceOf(ApplicationException.class)
                    .matches(ex -> ((ApplicationException) ex).getReason().getCode().equals("APPLICATION4003"));

            verify(mateRelationshipRepository, never()).save(any(MateRelationship.class));
            verify(applicationRepository, never()).save(any(Application.class));
        }

        @Test
        @DisplayName("이미 처리된 신청에 대해 예외를 발생시킨다")
        void shouldThrowExceptionForAlreadyProcessedApplication() {
            // Given
            Application processedApplication = Application.builder()
                    .id(1L)
                    .status(ApplicationStatus.SUCCEED)
                    .post(post)
                    .member(applicant)
                    .build();

            when(applicationRepository.findByIdAndPostMemberId(1L, 1L))
                    .thenReturn(Optional.of(processedApplication));

            // When & Then
            assertThatThrownBy(() -> applicationCommandService.updateStatusToSucceed(1L, 1L))
                    .isInstanceOf(ApplicationException.class)
                    .matches(ex -> ((ApplicationException) ex).getReason().getCode().equals("APPLICATION4001"));

            verify(mateRelationshipRepository, never()).save(any(MateRelationship.class));
            verify(applicationRepository, never()).save(any(Application.class));
        }
    }
}