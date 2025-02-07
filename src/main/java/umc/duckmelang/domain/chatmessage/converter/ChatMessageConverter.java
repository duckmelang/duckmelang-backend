package umc.duckmelang.domain.chatmessage.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import umc.duckmelang.domain.chatmessage.domain.ChatMessage;
import umc.duckmelang.domain.chatmessage.dto.ChatMessageRequestDto;
import umc.duckmelang.domain.chatmessage.dto.ChatMessageResponseDto;
import umc.duckmelang.domain.chatroom.domain.ChatRoom;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.ChatMessageException;

import java.time.LocalDateTime;

@Component
public class ChatMessageConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 채팅 메세지 객체 생성
    public static ChatMessage toChatMessage(ChatMessageRequestDto.@Valid CreateChatMessageDto request, ChatRoom chatRoom) {
        return ChatMessage.builder()
                .chatRoomId(chatRoom.getId())
                .senderId(request.getSenderId())
                .receiverId(request.getReceiverId())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .build();
    }

    // 채팅 메세지 DTO 생성
    public static ChatMessageResponseDto.ChatMessageDto toChatMessageDto(ChatMessage chatMessage) {
        return ChatMessageResponseDto.ChatMessageDto.builder()
                .id(chatMessage.getId())
                .chatRoomId(chatMessage.getChatRoomId())
                .senderId(chatMessage.getSenderId())
                .receiverId(chatMessage.getReceiverId())
                .content(chatMessage.getContent())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }

    // 통신 응답 메세지 생성
    public static TextMessage toTextMessage(ChatMessage chatMessage) {
        try {
            // DTO 생성
            ChatMessageResponseDto.ChatMessageDto response = toChatMessageDto(chatMessage);

            // JSON 변환 및 TextMessage 생성
            String responseJson = objectMapper.writeValueAsString(response);
            return new TextMessage(responseJson);

            // 에러케이스에 맞는 커스텀 예외처리 진행
        } catch (JsonMappingException e) {
            throw new ChatMessageException(ErrorStatus.INVALID_JSON_FORMAT);
        } catch (JsonProcessingException e) {
            throw new ChatMessageException(ErrorStatus.JSON_CONVERSION_FAILED);
        } catch (Exception e) {
            throw new ChatMessageException(ErrorStatus.JSON_PROCESSING_ERROR);
        }
    }

}
