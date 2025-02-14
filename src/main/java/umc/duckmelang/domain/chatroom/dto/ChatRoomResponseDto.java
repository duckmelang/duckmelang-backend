package umc.duckmelang.domain.chatroom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ChatRoomResponseDto {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatRoomItemListDto {
        private List<ChatRoomItemDto> chatRoomList;
        private Integer listSize;
        private Integer totalPage;
        private Long totalElements;
        private Boolean isFirst;
        private Boolean isLast;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatRoomItemDto{
        private Long chatRoomId;

        private Long oppositeId;
        private String oppositeNickname;
        private String oppositeProfileImage;

        private Long postId;
        private String postTitle;
        private String postImage;

        private String status;
        private Long applicationId;
        private String lastMessage;
        private LocalDateTime lastMessageTime;
    }
}
