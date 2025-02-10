package umc.duckmelang.domain.chatroom.service;

import umc.duckmelang.domain.chatroom.dto.ChatRoomResponseDto;

import java.util.List;

public interface ChatRoomQueryService {
    public List<ChatRoomResponseDto.ChatRoomItemDto> findAllChatRooms(Long memberId, int page);
    public List<ChatRoomResponseDto.ChatRoomItemDto> findOngoingChatRooms(Long memberId);
    public List<ChatRoomResponseDto.ChatRoomItemDto> findConfirmedChatRooms(Long memberId);
    public List<ChatRoomResponseDto.ChatRoomItemDto> findTerminatedChatRooms(Long memberId);
}
