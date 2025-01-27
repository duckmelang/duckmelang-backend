package umc.duckmelang.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import umc.duckmelang.domain.idolcategory.domain.IdolCategory;
import umc.duckmelang.domain.idolcategory.repository.IdolCategoryRepository;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.dto.MemberRequestDto;
import umc.duckmelang.domain.member.repository.MemberRepository;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository; // MemberRepository 주입

    @Autowired
    private IdolCategoryRepository idolCategoryRepository; // IdolCategoryRepository 주입

    private Long memberId;

    @BeforeEach
    void setUp() {
        // 테스트용 회원 생성 및 저장
        Member member = Member.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .birth(LocalDate.of(2001,12,1))
                .password("123")
                .gender(true)
                .build();
        member = memberRepository.save(member);
        memberId = member.getId(); // 생성된 회원 ID 저장

        // 테스트용 아이돌 카테고리 생성 및 저장
        IdolCategory idol1 = IdolCategory.builder()
                .name("BTS")
                .company("HYBE")
                .profileImage("bts_image_url")
                .build();

        IdolCategory idol2 = IdolCategory.builder()
                .name("Blackpink")
                .company("YG Entertainment")
                .profileImage("blackpink_image_url")
                .build();

        idolCategoryRepository.save(idol1);
        idolCategoryRepository.save(idol2);
    }

    @Test
    void selectMemberIdols_success() throws Exception {
        // Given
        MemberRequestDto.SelectIdolsDto request = MemberRequestDto.SelectIdolsDto.builder()
                .idolCategoryIds(List.of(1L, 2L)) // 미리 삽입한 아이돌 카테고리 ID 사용
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        // When & Then
        mockMvc.perform(post("/members/" + memberId + "/interesting-idol")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void selectMemberIdols_memberNotFound() throws Exception {
        // Given
        Long invalidMemberId = 999L; // 존재하지 않는 회원 ID

        MemberRequestDto.SelectIdolsDto request = MemberRequestDto.SelectIdolsDto.builder()
                .idolCategoryIds(List.of(1L))
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        // When & Then
        mockMvc.perform(post("/members/" + invalidMemberId + "/interesting-idol")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(content().string(containsString("존재하지 않는 회원입니다.")));
    }
}
