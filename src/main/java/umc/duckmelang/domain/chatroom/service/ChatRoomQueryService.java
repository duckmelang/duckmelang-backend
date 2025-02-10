package umc.duckmelang.domain.chatroom.service;

import umc.duckmelang.domain.chatroom.domain.ChatRoom;
import umc.duckmelang.domain.chatroom.dto.ChatRoomResponseDto;

import java.util.List;

public interface ChatRoomQueryService {
    public List<ChatRoomResponseDto.ChatRoomItemDto> findAllChatRooms();
    public List<ChatRoomResponseDto.ChatRoomItemDto> findOngoingChatRooms();
    public List<ChatRoomResponseDto.ChatRoomItemDto> findConfirmedChatRooms();
    public List<ChatRoomResponseDto.ChatRoomItemDto> findTerminatedChatRooms();
}
