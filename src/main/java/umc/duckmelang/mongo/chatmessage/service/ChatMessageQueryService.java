package umc.duckmelang.mongo.chatmessage.service;

import org.springframework.data.domain.Slice;
import umc.duckmelang.mongo.chatmessage.domain.ChatMessage;
import umc.duckmelang.mongo.chatmessage.dto.ChatMessageResponseDto;

import java.util.List;
import java.util.Map;

public interface ChatMessageQueryService {
    Slice<ChatMessage> getChatMessageListByChatRoom(Long ChatRoomId, String lastMessageId, int size);
    public Map<Long, ChatMessageResponseDto.LatestChatMessageDto> getLatestMessagesByChatRoomIds(List<Long> chatRoomIds);
}