package umc.duckmelang.domain.application.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("동행요청 연관관계 테스트")
public class ApplicationTest {
    @Test
    @DisplayName("북마크 생성 테스트")
    void addApplicationRelationTest(){
        //given
        Application application = Application.builder()
                .id(1L)
                .build();
        
    }
}
