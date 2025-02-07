package umc.duckmelang.domain.chatmessage.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.chatmessage.converter.ChatMessageConverter;
import umc.duckmelang.domain.chatmessage.domain.ChatMessage;
import umc.duckmelang.domain.chatmessage.dto.ChatMessageRequestDto;
import umc.duckmelang.domain.chatmessage.repository.ChatMessageRepository;
import umc.duckmelang.domain.chatroom.domain.ChatRoom;
import umc.duckmelang.domain.chatroom.repository.ChatRoomRepository;
import umc.duckmelang.domain.chatroom.service.ChatRoomCommandService;

@Service
@RequiredArgsConstructor
public class ChatMessageCommandServiceImpl implements ChatMessageCommandService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomCommandService chatRoomCommandService;

    @Override
    @Transactional
    public ChatMessage processMessage(ChatMessageRequestDto.@Valid CreateChatMessageDto request){
        // 1. 채팅방이 있는지 조회하고, 없으면 생성한다.
        ChatRoom chatRoom = chatRoomRepository.findByPostIdAndOtherMemberId(
                        request.getPostId(), request.getReceiverId())
                .orElseGet(() -> chatRoomCommandService.createChatRoom(request));

        // 2. 메세지를 MongoDB에 저장한다.
        ChatMessage newChatMessage = ChatMessageConverter.toChatMessage(request, chatRoom);
        return chatMessageRepository.save(newChatMessage);
    }

}
