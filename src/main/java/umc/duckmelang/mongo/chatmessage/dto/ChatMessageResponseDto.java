package umc.duckmelang.mongo.chatmessage.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import umc.duckmelang.global.common.serializer.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.List;

public class ChatMessageResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatMessageDto {
        private String id;
        private Long chatRoomId;
        private Long senderId;
        private Long receiverId;
        private String content;
        @JsonSerialize(using = LocalDateTimeSerializer.class)  // 커스텀 직렬화기 사용
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatMessageListDto {
        private List<ChatMessageDto> chatMessageList;
        private boolean hasNext;
        private String lastMessageId;  // 마지막 메시지 ID (다음 페이지 조회시 커서로 사용)
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class LatestChatMessageDto {
        private Long chatRoomId;
        private String messageId;
        private String content;
        private Long senderId;
        private LocalDateTime createdAt;
        private boolean isRead;
    }
}
