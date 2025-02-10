package umc.duckmelang.mongo.chatmessage.service;

import org.springframework.data.domain.Slice;
import umc.duckmelang.mongo.chatmessage.domain.ChatMessage;

public interface ChatMessageQueryService {
    Slice<ChatMessage> getChatMessageListByChatRoom(Long ChatRoomId, String lastMessageId, int size);
}