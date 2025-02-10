package umc.duckmelang.mongo.chatmessage.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.duckmelang.mongo.chatmessage.converter.ChatMessageConverter;
import umc.duckmelang.mongo.chatmessage.domain.ChatMessage;
import umc.duckmelang.mongo.chatmessage.dto.ChatMessageRequestDto;
import umc.duckmelang.domain.chatroom.converter.ChatRoomConverter;
import umc.duckmelang.domain.chatroom.domain.ChatRoom;
import umc.duckmelang.domain.chatroom.repository.ChatRoomRepository;
import umc.duckmelang.domain.chatroom.service.ChatRoomCommandService;
import umc.duckmelang.mongo.chatmessage.repository.ChatMessageRepository;

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
        ChatRoom chatRoom = chatRoomRepository.findByPostIdAndOtherMemberId(request.getPostId(), request.getReceiverId())
                .or(() -> chatRoomRepository.findByPostIdAndOtherMemberId(request.getPostId(), request.getSenderId()))
                .orElseGet(() -> {
                    ChatRoom newChatRoom = ChatRoomConverter.toChatRoom(request);
                    return chatRoomRepository.save(newChatRoom); // 채팅방 생성 후 DB에 저장
                });

        // 2. 메세지를 MongoDB에 저장한다.
        ChatMessage newChatMessage = ChatMessageConverter.toChatMessage(request, chatRoom);
        return chatMessageRepository.save(newChatMessage);
    }
}
