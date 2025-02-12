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
                    "    _id: '$chatRoomId', " +
                    "    messageId: { $first: '$_id' }, " +
                    "    content: { $first: '$content' }, " +
                    "    createdAt: { $first: '$createdAt' } " +
                    "} }"
    })
    List<ChatMessageResponseDto.LatestChatMessageDto> findLatestMessagesByChatRoomIds(List<Long> chatRoomIds);
}
