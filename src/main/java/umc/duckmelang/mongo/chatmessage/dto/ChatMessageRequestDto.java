package umc.duckmelang.mongo.chatmessage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChatMessageRequestDto {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateChatMessageDto {
        @NotNull
        private Long senderId;
        @NotNull
        private Long receiverId;
        @NotNull
        private Long postId;
        @NotBlank
        private String content;
    }
}
