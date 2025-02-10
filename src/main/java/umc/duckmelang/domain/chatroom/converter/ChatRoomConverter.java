package umc.duckmelang.domain.chatroom.converter;

import org.springframework.stereotype.Component;
import umc.duckmelang.mongo.chatmessage.dto.ChatMessageRequestDto;
import umc.duckmelang.domain.chatroom.domain.ChatRoom;

@Component
public class ChatRoomConverter {

    public static ChatRoom toChatRoom(ChatMessageRequestDto.CreateChatMessageDto request) {
        return ChatRoom.builder()
                .postId(request.getPostId())
                .otherMemberId(request.getReceiverId())
                .hasMatched(false)                    // 채팅방이 처음 생성된 시점에는 반드시 매칭 전이다.
                .hasSenderReviewDone(false)           // 채팅방이 처음 생성된 시점에는 반드시 매칭 전이므로 리뷰 또한 없다.
                .hasReceiverReviewDone(false)
                .build();
    }
}