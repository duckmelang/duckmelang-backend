package umc.duckmelang.domain.member.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import umc.duckmelang.domain.eventcategory.domain.EventCategory;
import umc.duckmelang.domain.eventcategory.repository.EventCategoryRepository;
import umc.duckmelang.domain.idolcategory.domain.IdolCategory;
import umc.duckmelang.domain.idolcategory.repository.IdolCategoryRepository;
import umc.duckmelang.domain.landmine.domain.Landmine;
import umc.duckmelang.domain.landmine.repository.LandmineRepository;
import umc.duckmelang.domain.member.domain.Member;

import umc.duckmelang.domain.member.dto.MemberRequestDto;
import umc.duckmelang.domain.member.repository.MemberRepository;
import umc.duckmelang.domain.memberevent.domain.MemberEvent;
import umc.duckmelang.domain.memberevent.repository.MemberEventRepository;
import umc.duckmelang.domain.memberidol.domain.MemberIdol;
import umc.duckmelang.domain.memberidol.repository.MemberIdolRepository;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.repository.MemberProfileImageRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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

    @Mock
    private EventCategoryRepository eventCategoryRepository;

    @Mock
    private MemberEventRepository memberEventRepository;

    @Mock
    private LandmineRepository landmineRepository;

    @Mock
    private MemberProfileImageRepository memberProfileImageRepository;


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
        List<MemberIdol> mockMemberIdolList = List.of(
                new MemberIdol(1L, idol1, mockMember),
                new MemberIdol(2L, idol2, mockMember)
        );

        when(memberIdolRepository.saveAll(anyList())).thenReturn(mockMemberIdolList);
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




    // 어떤 행사도 선택하지 않은 경우
    @Test
    public void testSelectEvents_WithNoSelectedEvents() {
        Long memberId = 1L;

        // Mock Member 생성
        Member mockMember = Member.builder()
                .id(memberId)
                .name("Test User")
                .email("test@example.com")
                .build();
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(mockMember));


        MemberRequestDto.SelectEventsDto request = MemberRequestDto.SelectEventsDto.builder()
                .eventCategoryIds(Collections.emptyList())
                .build();

        List<MemberEvent> result = memberCommandService.selectEvents(memberId, request);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "결과 리스트는 비어 있어야 합니다.");
    }

    // 행사를 두 가지 선택한 경우
    @Test
    public void testSelectEvents_WithSelectedEvents() {
        Long memberId = 1L;
        List<Long> eventCategoryIds = List.of(1L, 2L);
        MemberRequestDto.SelectEventsDto request = MemberRequestDto.SelectEventsDto.builder()
                .eventCategoryIds(eventCategoryIds)
                .build();

        // Mock Member 생성
        Member mockMember = Member.builder()
                .id(memberId)
                .name("Test User")
                .email("test@example.com")
                .build();
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(mockMember));


        // Mock EventCategory 생성
        EventCategory event1 = EventCategory.builder()
                .id(1L)
                .name("생일카페")
                .build();

        EventCategory event2 = EventCategory.builder()
                .id(2L)
                .name("콘서트")
                .build();

        when(eventCategoryRepository.findAllById(eventCategoryIds)).thenReturn(List.of(event1, event2));

        // Mock saveAll() 메서드 설정
        List<MemberEvent> mockMemberEventList = List.of(
                new MemberEvent(1L, event1, mockMember),
                new MemberEvent(2L, event2, mockMember)
        );

        when(memberEventRepository.saveAll(anyList())).thenReturn(mockMemberEventList);
        // When
        List<MemberEvent> result = memberCommandService.selectEvents(memberId, request);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size(), "결과 리스트의 크기는 2여야 한다.");
    }




    // 키워드가 겹치지 않도록 지뢰를 3가지 설정하는 경우
    @Test
    public void createLandmines_success() {
        // Given
        Long memberId = 1L;

        // Mock Member 객체 생성
        Member member = Member.builder()
                .id(memberId)
                .name("Test User")
                .email("test@example.com")
                .build();

        List<String> landmineContents = Arrays.asList("지뢰1", "지뢰2", "지뢰3");
        MemberRequestDto.CreateLandminesDto requestDto = MemberRequestDto.CreateLandminesDto.builder()
                .landmineContents(landmineContents)
                .build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        // Mock deleteAllByMember() 메서드 설정
        doNothing().when(landmineRepository).deleteAllByMember(member);

        // Mock saveAll() 메서드 설정
        List<Landmine> mockLandmineList = landmineContents.stream()
                .map(content -> new Landmine(null, content, member)) // ID는 null로 설정
                .toList();

        when(landmineRepository.saveAll(anyList())).thenReturn(mockLandmineList);

        // When
        List<Landmine> result = memberCommandService.createLandmines(memberId, requestDto);

        // Then
        assertThat(result).hasSize(3);
        verify(memberRepository, times(1)).findById(memberId);
        verify(landmineRepository, times(1)).deleteAllByMember(member);
        verify(landmineRepository, times(1)).saveAll(anyList());
    }

    // 지뢰 키워드를 단 하나도 설정하지 않은 경우
    @Test
    public void createLandmines_withEmptyContents_shouldReturnEmptyList() {
        // Given
        Long memberId = 1L;

        // Mock Member 객체 생성
        Member member = Member.builder()
                .id(memberId)
                .name("Test User")
                .email("test@example.com")
                .build();

        MemberRequestDto.CreateLandminesDto requestDto = MemberRequestDto.CreateLandminesDto.builder()
                .landmineContents(Collections.emptyList())
                .build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        // When
        List<Landmine> result = memberCommandService.createLandmines(memberId, requestDto);

        // Then
        assertThat(result).isEmpty();
        verify(landmineRepository, never()).deleteAllByMember(any());
    }

    // 중복되는 지뢰 키워드를 입력했을 경우
    @Test
    public void createLandmines_withDuplicateKeywords_shouldThrowException() {
        // Given
        Long memberId = 1L;

        // Mock Member 객체 생성
        Member member = Member.builder()
                .id(memberId)
                .name("Test User")
                .email("test@example.com")
                .build();

        List<String> landmineContents = Arrays.asList("지뢰1", "지뢰2", "지뢰1"); // 중복된 키워드 포함
        MemberRequestDto.CreateLandminesDto requestDto = MemberRequestDto.CreateLandminesDto.builder()
                .landmineContents(landmineContents)
                .build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberCommandService.createLandmines(memberId, requestDto);
        });

        assertThat(exception.getMessage()).isEqualTo("중복된 키워드가 존재합니다: 지뢰1");
        verify(landmineRepository, never()).deleteAllByMember(any());
    }







    // 프로필 이미지를 설정하지 않은 경우 (기본 이미지 URL 반환)
    @Test
    public void createMemberProfileImage_withEmptyProfileImage_shouldUseDefaultImage() {
        // Given
        Long memberId = 1L;
        String profileImageUrl = "";


        // Mock Member 객체 생성
        Member member = Member.builder()
                .id(memberId)
                .name("Test User")
                .email("test@example.com")
                .build();

        // Mock MemberProfileImage 객체 (기본 이미지 설정)
        MemberProfileImage savedProfileImage = MemberProfileImage.builder()
                .id(1L) // 저장 후 ID가 생성된 상태를 가정
                .member(member)
                .memberImage("default-profile-image-url") // 기본 프로필 이미지 URL
                .build();


        MemberRequestDto.CreateMemberProfileImageDto requestDto = MemberRequestDto.CreateMemberProfileImageDto.builder()
                .memberProfileImageURL(profileImageUrl)
                .build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberProfileImageRepository.save(any(MemberProfileImage.class))).thenReturn(savedProfileImage);

        // When
        MemberProfileImage result = memberCommandService.createMemberProfileImage(memberId, requestDto);

        // Then
        assertThat(result.getMemberImage()).isEqualTo("default-profile-image-url"); // 기본 이미지 URL 검증
        verify(memberProfileImageRepository).deleteAllByMember(member); // 기존 데이터 삭제 확인
        verify(memberProfileImageRepository).save(any(MemberProfileImage.class)); // 새 데이터 저장 확인
    }

    // 유효한 프로필 이미지를 설정한 경우
    @Test
    public void createMemberProfileImage_withValidProfileImage_shouldSaveSuccessfully() {
        // Given
        Long memberId = 1L;
        String profileImageUrl = "https://example.com/profile-image.jpg";

        // Mock Member 객체 생성
        Member member = Member.builder()
                .id(memberId)
                .name("Test User")
                .email("test@example.com")
                .build();

        MemberRequestDto.CreateMemberProfileImageDto requestDto = MemberRequestDto.CreateMemberProfileImageDto.builder()
                .memberProfileImageURL(profileImageUrl)
                .build();

        // Mock MemberProfileImage 객체 (기본 이미지 설정)
        MemberProfileImage savedProfileImage = MemberProfileImage.builder()
                .id(1L) // 저장 후 ID가 생성된 상태를 가정
                .member(member)
                .memberImage(profileImageUrl)
                .build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberProfileImageRepository.save(any(MemberProfileImage.class))).thenReturn(savedProfileImage);


        // When
        MemberProfileImage result = memberCommandService.createMemberProfileImage(memberId, requestDto);

        // Then
        assertThat(result.getMemberImage()).isEqualTo(profileImageUrl); // 설정된 이미지 URL 검증
        assertThat(result.getMember()).isEqualTo(member); // Member와 연관 관계 검증
        verify(memberProfileImageRepository).deleteAllByMember(member); // 기존 데이터 삭제 확인
        verify(memberProfileImageRepository).save(any(MemberProfileImage.class)); // 새 데이터 저장 확인
    }

    // 존재하지 않는 회원 ID를 전달한 경우
    @Test
    public void createMemberProfileImage_withInvalidMemberId_shouldThrowException() {
        // Given
        Long memberId = 999L; // 존재하지 않는 ID
        MemberRequestDto.CreateMemberProfileImageDto requestDto = MemberRequestDto.CreateMemberProfileImageDto.builder()
                .memberProfileImageURL("https://example.com/profile-image.jpg")
                .build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty()); // 회원이 조회되지 않음

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberCommandService.createMemberProfileImage(memberId, requestDto);
        });

        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 회원입니다."); // 예외 메시지 검증
        verify(memberProfileImageRepository, never()).deleteAllByMember(any()); // 삭제 호출되지 않음
        verify(memberProfileImageRepository, never()).save(any()); // 저장 호출되지 않음
    }


    // 유효한 자기소개 문구를 설정한 경우
    @Test
    void createIntroduction_withValidIntroduction_shouldSaveSuccessfully() {
        // Given
        Long memberId = 1L;
        String introduction = "안녕하세요! 저는 새로운 회원입니다.";

        // Mock Member 객체 생성
        Member member = Member.builder()
                .id(memberId)
                .name("Test User")
                .email("test@example.com")
                .build();

        MemberRequestDto.CreateIntroductionDto requestDto = MemberRequestDto.CreateIntroductionDto.builder()
                .introduction(introduction)
                .build();

        // Mock Member 객체 (업데이트된 자기소개 포함)
        Member updatedMember = Member.builder()
                .id(memberId)
                .name("Test User")
                .email("test@example.com")
                .introduction(introduction)
                .build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberRepository.save(any(Member.class))).thenReturn(updatedMember);

        // When
        Member result = memberCommandService.createIntroduction(memberId, requestDto);

        // Then
        assertThat(result.getIntroduction()).isEqualTo(introduction); // 설정된 자기소개 검증
        assertThat(result.getId()).isEqualTo(memberId); // Member ID 검증
        verify(memberRepository).findById(memberId); // 회원 조회 확인
        verify(memberRepository).save(any(Member.class)); // 새 데이터 저장 확인
    }


    // 회원이 존재하지 않는 경우
    @Test
    void createIntroduction_memberNotFound_shouldThrowException() {
        // Given
        Long memberId = 1L;
        MemberRequestDto.CreateIntroductionDto requestDto = MemberRequestDto.CreateIntroductionDto.builder()
                .introduction("안녕하세요!")
                .build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            memberCommandService.createIntroduction(memberId, requestDto);
        });

        verify(memberRepository).findById(memberId); // 회원 조회 확인
        verify(memberRepository, never()).save(any(Member.class)); // 저장하지 않음 확인
    }

    // 공백만 입력한 경우
    @Test
    void createIntroduction_withOnlyWhitespace_shouldThrowException() {
        // Given
        Long memberId = 1L;
        String introduction = "   "; // 공백만 입력

        // Mock Member 객체 생성
        Member member = Member.builder()
                .id(memberId)
                .name("Test User")
                .email("test@example.com")
                .build();

        MemberRequestDto.CreateIntroductionDto requestDto = MemberRequestDto.CreateIntroductionDto.builder()
                .introduction(introduction)
                .build();

        // Mock Member 객체 (업데이트된 자기소개 포함)
        Member updatedMember = Member.builder()
                .id(memberId)
                .name("Test User")
                .email("test@example.com")
                .introduction(introduction)
                .build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberRepository.save(any(Member.class))).thenReturn(updatedMember);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            memberCommandService.createIntroduction(memberId, requestDto);
        });

        verify(memberRepository).findById(memberId); // 회원 조회 확인
        verify(memberRepository, never()).save(any(Member.class)); // 저장하지 않음 확인
    }





}



