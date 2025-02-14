package umc.duckmelang.mongo.chatmessage.repository;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import umc.duckmelang.mongo.chatmessage.domain.ChatMessage;
import umc.duckmelang.mongo.chatmessage.dto.ChatMessageResponseDto;

import java.util.List;

// ChatMessage는 MongoDB 기반이기 때문에 JPARepository가 아니라 MongoRepository로 인터페이스를 확장한다.
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    @Aggregation(pipeline = {
            "{ $match: { chatRoomId: { $in: ?0 } } }",
            "{ $sort: { chatRoomId: 1, createdAt: -1 } }",
            "{ $group: { " +
                    "_id: \"$chatRoomId\", " +  // chatRoomId를 그룹화 키로 사용
                    "messageId: { $first: \"$_id\" }, " +  // 첫 번째 메시지 ID
                    "content: { $first: \"$content\" }, " +  // 첫 번째 컨텐츠
                    "createdAt: { $first: \"$createdAt\" }, " +  // 첫 번째 생성 시간
                    "chatRoomId: { $first: \"$chatRoomId\" } " +  // chatRoomId 유지
                    "} }"
    })
    List<ChatMessageResponseDto.LatestChatMessageDto> findLatestMessagesByChatRoomIds(List<Long> chatRoomIds);
}
