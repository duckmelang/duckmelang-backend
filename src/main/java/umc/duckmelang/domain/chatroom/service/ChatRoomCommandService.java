package umc.duckmelang.domain.chatroom.service;

import umc.duckmelang.domain.chatmessage.dto.ChatMessageRequestDto;
import umc.duckmelang.domain.chatroom.domain.ChatRoom;

public interface ChatRoomCommandService {
    ChatRoom createChatRoom(ChatMessageRequestDto.CreateChatMessageDto request);
}
