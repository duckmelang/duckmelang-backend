package umc.duckmelang.domain.member.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.repository.MemberRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class MemberQueryServiceTest {

    @InjectMocks
    private MemberQueryServiceImpl memberQueryService;

    @Mock
    private MemberRepository memberRepository;

    public MemberQueryServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void calculateMemberAge_success() {
        // Given
        Long memberId = 1L;

        // Mock Member 생성
        Member member = Member.builder()
                .id(memberId)
                .name("Test User")
                .email("test@example.com")
                .birth(LocalDate.of(2001, 12, 1))
                .build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        int age = memberQueryService.calculateMemberAge(memberId);

        assertEquals(23, age);


    }


}
