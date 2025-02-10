package umc.duckmelang.domain.chatroom.converter;

import org.springframework.stereotype.Component;
import umc.duckmelang.mongo.chatmessage.dto.ChatMessageRequestDto;
import umc.duckmelang.domain.chatroom.domain.ChatRoom;
import umc.duckmelang.domain.chatroom.dto.ChatRoomResponseDto;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.post.domain.Post;

import java.util.List;

@Component
public class ChatRoomConverter {
    public static ChatRoom toChatRoom(ChatMessageRequestDto.CreateChatMessageDto request) {
        return ChatRoom.builder()
                .post(null)
                .otherMemberId(null)
                .hasMatched(false)                    // 채팅방이 처음 생성된 시점에는 반드시 매칭 전이다.
                .hasSenderReviewDone(false)           // 채팅방이 처음 생성된 시점에는 반드시 매칭 전이므로 리뷰 또한 없다.
                .hasReceiverReviewDone(false)
                .build();
    }

    public static ChatRoomResponseDto.ChatRoomItemDto toChatRoomItemDto(ChatRoom chatRoom, Member member, Post post) {
        //TODO
        return ChatRoomResponseDto.ChatRoomItemDto.builder().build();
    }

    public static ChatRoomResponseDto.ChatRoomItemListDto toChatRoomItemListDto(List<ChatRoomResponseDto.ChatRoomItemDto> chatRooms) {
        //TODO
        return null;
    }
}