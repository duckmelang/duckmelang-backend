package umc.duckmelang.domain.chatmessage.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import umc.duckmelang.domain.chatmessage.domain.ChatMessage;

// ChatMessage는 MongoDB 기반이기 때문에 JPARepository가 아니라 MongoRepository로 인터페이스를 확장한다.
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
}
