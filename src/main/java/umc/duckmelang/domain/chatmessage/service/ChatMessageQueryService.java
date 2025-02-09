package umc.duckmelang.domain.chatmessage.service;

import org.springframework.data.domain.Slice;
import umc.duckmelang.domain.chatmessage.domain.ChatMessage;

public interface ChatMessageQueryService {
    Slice<ChatMessage> getChatMessageListByChatRoom(Long ChatRoomId, String lastMessageId, int size);
}