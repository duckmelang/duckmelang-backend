package umc.duckmelang.domain.chatmessage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;
import umc.duckmelang.domain.chatmessage.converter.ChatMessageConverter;
import umc.duckmelang.domain.chatmessage.dto.ChatMessageResponseDto;
import umc.duckmelang.domain.chatmessage.service.ChatMessageQueryService;
import umc.duckmelang.global.apipayload.ApiResponse;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatMessageWebSocketController {

    private final ChatMessageQueryService chatMessageQueryService;

    @GetMapping("/{chatRoomId}/messages")
    @Operation(summary = "특정 채팅방 내 채팅 조회 API", description = "채팅방 내 채팅을 20개씩 조회합니다. 무한 스크롤 방식으로 조회합니다.")
    @Parameters({@Parameter(name = "chatRoomId", description = "조회할 채팅방 Id, path variable 입니다!"),
                   @Parameter(name = "lastMessageId", description = "처음 조회 시에는 입력하지 않습니다. 무한 스크롤 시, 첫 조회의 Response Body에 있는 lastMessageId를 입력하시면 됩니다.")})
    public ApiResponse<ChatMessageResponseDto.ChatMessageListDto> getChatMessagesByChatRoom(
            @PathVariable("chatRoomId") Long chatRoomId,
            @RequestParam(required = false) String lastMessageId,
            @RequestParam(defaultValue = "20") int size) {

        Slice<ChatMessage> chatMessageList = chatMessageQueryService.getChatMessageListByChatRoom(chatRoomId, lastMessageId, size);

        return ApiResponse.onSuccess(ChatMessageConverter.toChatMessageListDto(chatMessageList));
    }
}