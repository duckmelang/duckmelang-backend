package umc.duckmelang.domain.member.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import umc.duckmelang.domain.idolcategory.domain.IdolCategory;
import umc.duckmelang.domain.idolcategory.repository.IdolCategoryRepository;
import umc.duckmelang.domain.member.domain.Member;

import umc.duckmelang.domain.member.dto.MemberRequestDto;
import umc.duckmelang.domain.member.repository.MemberRepository;
import umc.duckmelang.domain.memberidol.domain.MemberIdol;
import umc.duckmelang.domain.memberidol.repository.MemberIdolRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MemberCommandServiceTest {

    @InjectMocks
    private MemberCommandServiceImpl memberCommandService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private IdolCategoryRepository idolCategoryRepository;

    @Mock
    private MemberIdolRepository memberIdolRepository;

    public MemberCommandServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void selectIdols_success() {
        // Given
        Long memberId = 1L;
        List<Long> idolCategoryIds = List.of(1L, 2L);
        MemberRequestDto.SelectIdolsDto request = MemberRequestDto.SelectIdolsDto.builder()
                .idolCategoryIds(idolCategoryIds)
                .build();

        // Mock Member 생성
        Member mockMember = Member.builder()
                .id(memberId)
                .name("Test User")
                .email("test@example.com")
                .build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(mockMember));

        // Mock IdolCategory 생성
        IdolCategory idol1 = IdolCategory.builder()
                .id(1L)
                .name("BTS")
                .company("HYBE")
                .profileImage("bts_image_url")
                .build();

        IdolCategory idol2 = IdolCategory.builder()
                .id(2L)
                .name("Blackpink")
                .company("YG Entertainment")
                .profileImage("blackpink_image_url")
                .build();

        when(idolCategoryRepository.findAllById(idolCategoryIds)).thenReturn(List.of(idol1, idol2));

        // Mock saveAll() 메서드 설정
        List<MemberIdol> mockMemberIdols = List.of(
                new MemberIdol(1L, idol1, mockMember),
                new MemberIdol(2L, idol2, mockMember)
        );

        when(memberIdolRepository.saveAll(anyList())).thenReturn(mockMemberIdols);
        // When
        List<MemberIdol> result = memberCommandService.selectIdols(memberId, request);

        // Then
        assertThat(result).hasSize(2);
        verify(memberRepository, times(1)).findById(memberId);
        verify(memberIdolRepository, times(1)).deleteAllByMember(mockMember);
        verify(memberIdolRepository, times(1)).saveAll(anyList());
    }

    // 존재하지 않는 회원이 아이돌 선택을 시도하는 경우
    @Test
    void selectIdols_memberNotFound() {
        // Given
        Long memberId = 999L;
        List<Long> idolCategoryIds = List.of(1L);

        MemberRequestDto.SelectIdolsDto request = MemberRequestDto.SelectIdolsDto.builder()
                .idolCategoryIds(idolCategoryIds)
                .build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> memberCommandService.selectIdols(memberId, request));
        verify(memberRepository, times(1)).findById(memberId);
    }

    // 유효하지 않은 아이돌 카테고리 ID가 포함된 경우
    @Test
    void selectMemberIdols_invalidIdolCategory() {
        // Given
        Long memberId = 1L;
        List<Long> idolCategoryIds = List.of(1L, 999L); // 999L은 존재하지 않는 ID

        MemberRequestDto.SelectIdolsDto request = MemberRequestDto.SelectIdolsDto.builder()
                .idolCategoryIds(idolCategoryIds)
                .build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(Member.builder().id(memberId).build()));

        // Mock: 존재하는 아이돌 카테고리만 반환하도록 설정
        when(idolCategoryRepository.findAllById(idolCategoryIds)).thenReturn(List.of(IdolCategory.builder().id(1L).name("BTS").build()));
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> memberCommandService.selectIdols(memberId, request));
    }






}
