package umc.duckmelang.domain.memberprofileimage.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.repository.MemberRepository;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.repository.MemberProfileImageRepository;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class MemberProfileImageQueryServiceTest {

    @InjectMocks
    private MemberProfileImageQueryServiceImpl memberProfileImageQueryService;

    @Mock
    private MemberProfileImageRepository memberProfileImageRepository;

    @Mock
    private MemberRepository memberRepository;

    public MemberProfileImageQueryServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getLatestPublicMemberProfileImage_shouldReturnLatestPublicProfileImage(){

        // Given
        Long memberId = 1L;
        Member member = Member.builder()
                .id(memberId)
                .name("Test User")
                .email("test@example.com")
                .build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));


        MemberProfileImage olderProfileImage = MemberProfileImage.builder()
                .id(2L)
                .member(member)
                .memberImage("이미지주소1")
                .isPublic(true)
                .build();

        MemberProfileImage latestProfileImage = MemberProfileImage.builder()
                .id(1L)
                .member(member)
                .memberImage("이미지주소2")
                .isPublic(true)
                .build();


        when(memberProfileImageRepository.findFirstByMemberIdAndIsPublicTrueOrderByCreatedAtDesc(member.getId()))
                .thenReturn(Optional.of(latestProfileImage));

        // When
        Optional<MemberProfileImage> result = memberProfileImageQueryService.getLatestPublicMemberProfileImage(member.getId());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(latestProfileImage.getId());
        assertThat(result.get().getMember()).isEqualTo(latestProfileImage.getMember());
        assertThat(result.get().isPublic()).isTrue();
        assertThat(result.get().getCreatedAt()).isEqualTo(latestProfileImage.getCreatedAt());

    }
}
