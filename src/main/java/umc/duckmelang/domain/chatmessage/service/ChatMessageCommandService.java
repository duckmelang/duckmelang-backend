package umc.duckmelang.domain.chatmessage.service;

import jakarta.validation.Valid;
import umc.duckmelang.domain.chatmessage.domain.ChatMessage;
import umc.duckmelang.domain.chatmessage.dto.ChatMessageRequestDto;

public interface ChatMessageCommandService {
    ChatMessage processMessage(ChatMessageRequestDto.@Valid CreateChatMessageDto request);
}