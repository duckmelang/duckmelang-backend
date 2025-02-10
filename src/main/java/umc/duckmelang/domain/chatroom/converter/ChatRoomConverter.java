package umc.duckmelang.domain.chatroom.converter;

import umc.duckmelang.domain.chatroom.domain.ChatRoom;
import umc.duckmelang.domain.chatroom.dto.ChatRoomResponseDto;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.post.domain.Post;

import java.util.List;

public class ChatRoomConverter {
    public static ChatRoomResponseDto.ChatRoomItemDto toChatRoomItemDto(ChatRoom chatRoom, Member member, Post post) {
        //TODO
        return ChatRoomResponseDto.ChatRoomItemDto.builder()
                .build();
    }

    public static ChatRoomResponseDto.ChatRoomItemListDto toChatRoomItemListDto(List<ChatRoomResponseDto.ChatRoomItemDto> chatRooms) {
        //TODO
        return null;
    }
}
