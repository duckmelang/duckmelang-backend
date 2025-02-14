package umc.duckmelang.domain.chatroom.service;

import org.springframework.data.domain.Page;
import umc.duckmelang.domain.chatroom.dto.ChatRoomResponseDto;

public interface ChatRoomQueryService {
    public Page<ChatRoomResponseDto.ChatRoomItemDto> findAllChatRooms(Long memberId, int page);
    public Page<ChatRoomResponseDto.ChatRoomItemDto> findOngoingChatRooms(Long memberId, int page);
    public Page<ChatRoomResponseDto.ChatRoomItemDto> findConfirmedChatRooms(Long memberId, int page);
    public Page<ChatRoomResponseDto.ChatRoomItemDto> findTerminatedChatRooms(Long memberId, int page);
}
