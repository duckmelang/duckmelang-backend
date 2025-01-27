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


}
