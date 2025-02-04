package umc.duckmelang.domain.chatmessage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import umc.duckmelang.domain.chatmessage.domain.ChatMessage;
import umc.duckmelang.domain.chatmessage.repository.ChatMessageRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ChatMessageRepositoryTest {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Test
    void saveChatMessage_Success() {
        // Given: 테스트용 ChatMessage 객체 생성
        ChatMessage chatMessage = ChatMessage.builder()
                .senderId(1L)  // 보낸 사람 ID
                .receiverId(2L)  // 받은 사람 ID
                .chatRoomId(100L)  // 채팅방 ID
                .content("잘 생성되는지 확인하는 중")  // 메시지 내용
                .createdAt(LocalDateTime.now())  // 생성 시간
                .build();

        // When: Repository를 통해 데이터 저장
        ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);

        // Then: 저장된 데이터 검증
        assertThat(savedChatMessage).isNotNull();  // 저장된 객체가 null이 아님을 확인
        assertThat(savedChatMessage.getId()).isNotNull();  // MongoDB에서 자동 생성된 ID가 있음
        assertThat(savedChatMessage.getSenderId()).isEqualTo(1L);  // 보낸 사람 ID 확인
        assertThat(savedChatMessage.getContent()).isEqualTo("잘 생성되는지 확인하는 중");  // 메시지 내용 확인

        System.out.println("저장된 메시지 ID: " + savedChatMessage.getId());
    }
}
