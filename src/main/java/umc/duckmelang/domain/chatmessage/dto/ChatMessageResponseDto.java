package umc.duckmelang.domain.chatmessage.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.duckmelang.global.common.serializer.LocalDateTimeSerializer;

import java.time.LocalDateTime;

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


//    public class ChatMessageListDto {
//    }


}
