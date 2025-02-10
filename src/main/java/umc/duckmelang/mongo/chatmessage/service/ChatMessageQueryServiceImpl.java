package umc.duckmelang.mongo.chatmessage.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import umc.duckmelang.mongo.chatmessage.domain.ChatMessage;
import umc.duckmelang.mongo.chatmessage.repository.ChatMessageRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageQueryServiceImpl implements ChatMessageQueryService {

    private final ChatMessageRepository chatMessageRepository;
    private final MongoTemplate mongoTemplate; // QueryDSL 대체로 사용

    @Override
    public Slice<ChatMessage> getChatMessageListByChatRoom(Long chatRoomId, String lastMessageId, int size) {
        // MongoDB 데이터 조회
        List<ChatMessage> chatMessageList = fetchMessages(chatRoomId, lastMessageId, size);

        // 다음 페이지 여부 계산
        boolean hasNext = hasNextPage(chatMessageList, size);

        // Slice 객체 반환
        return new SliceImpl<>(chatMessageList, PageRequest.of(0, size), hasNext);
    }


    private List<ChatMessage> fetchMessages(Long chatRoomId, String lastMessageId, int size) {
        // 커서 기반 조회를 위한 조건 설정
        Criteria criteria = Criteria.where("chatRoomId").is(chatRoomId);

        // 커서가 존재하면 lastMessageId보다 작은 _id만 조회
        if (lastMessageId != null) {
            criteria = criteria.and("_id").lt(new ObjectId(lastMessageId));
        }

        Query query = new Query(criteria)
                .with(Sort.by(Sort.Direction.DESC, "createdAt")) // 최신순 정렬
                .limit(size + 1); // 페이지 크기보다 1개 더 가져와서 hasNext 판단

        return mongoTemplate.find(query, ChatMessage.class);
    }



    private boolean hasNextPage(List<ChatMessage> messages, int pageSize) {
        if (messages.size() > pageSize) {
            messages.remove(pageSize); // 초과 데이터 제거
            return true;
        }
        return false;
    }
}