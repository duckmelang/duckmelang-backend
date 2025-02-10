package umc.duckmelang.mongo.chatmessage.service;

import jakarta.validation.Valid;
import umc.duckmelang.mongo.chatmessage.domain.ChatMessage;
import umc.duckmelang.mongo.chatmessage.dto.ChatMessageRequestDto;

public interface ChatMessageCommandService {
    ChatMessage processMessage(ChatMessageRequestDto.@Valid CreateChatMessageDto request);
}