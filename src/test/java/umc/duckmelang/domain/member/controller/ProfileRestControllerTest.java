package umc.duckmelang.domain.member.controller;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    @InjectMocks
    private ProfileController profileController;

    @Mock
    private ProfileFacadeService profileFacadeService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(profileController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("다른 멤버 프로필 조회 성공 테스트")
    void getOtherProfile_Success() throws Exception {
        // Given
        Long memberId = 1L;
        int page = 0;

        Member member = Member.builder()
                .id(memberId)
                .nickname("testUser")
                .build();

        Page<PostThumbnailResponseDto> imagePage = new PageImpl<>(
                List.of(new PostThumbnailResponseDto("image_url")));

        MemberProfileImage profileImage = MemberProfileImage.builder()
                .imageUrl("profile_image_url")
                .build();

        MemberResponseDto.OtherProfileDto expectedResponse = MemberConverter.ToOtherProfileDto(
                member, 5, 3, profileImage, imagePage);

        when(profileFacadeService.getOtherProfileByMemberId(memberId, page))
                .thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/profiles/{memberId}", memberId)
                        .param("page", String.valueOf(page))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value(SuccessStatus._OK.getCode()))
                .andExpect(jsonPath("$.message").value(SuccessStatus._OK.getMessage()))
                .andExpect(jsonPath("$.result").exists());

        verify(profileFacadeService).getOtherProfileByMemberId(memberId, page);
    }

    @Test
    @DisplayName("존재하지 않는 멤버 프로필 조회 실패 테스트")
    void getOtherProfile_MemberNotFound() throws Exception {
        // Given
        Long nonExistentMemberId = 999L;
        int page = 0;

        when(profileFacadeService.getOtherProfileByMemberId(nonExistentMemberId, page))
                .thenThrow(new MemberHandler(ErrorStatus.NO_SUCH_MEMBER));

        // When & Then
        mockMvc.perform(get("/api/v1/profiles/{memberId}", nonExistentMemberId)
                        .param("page", String.valueOf(page))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value(ErrorStatus.NO_SUCH_MEMBER.getCode()))
                .andExpect(jsonPath("$.message").exists());

        verify(profileFacadeService).getOtherProfileByMemberId(nonExistentMemberId, page);
    }

    @Test
    @DisplayName("잘못된 페이지 번호로 프로필 조회 실패 테스트")
    void getOtherProfile_InvalidPage() throws Exception {
        // Given
        Long memberId = 1L;
        int invalidPage = -1;

        // When & Then
        mockMvc.perform(get("/api/v1/profiles/{memberId}", memberId)
                        .param("page", String.valueOf(invalidPage))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}