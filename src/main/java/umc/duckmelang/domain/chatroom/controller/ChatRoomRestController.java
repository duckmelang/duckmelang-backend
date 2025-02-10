package umc.duckmelang.domain.chatroom.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.duckmelang.domain.chatroom.converter.ChatRoomConverter;
import umc.duckmelang.domain.chatroom.dto.ChatRoomResponseDto;
import umc.duckmelang.domain.chatroom.service.ChatRoomQueryService;
import umc.duckmelang.global.annotations.CommonApiResponses;
import umc.duckmelang.global.apipayload.ApiResponse;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/chatrooms")
@RequiredArgsConstructor
public class ChatRoomRestController {
    private ChatRoomQueryService chatRoomQueryService;

    @GetMapping("")
    @CommonApiResponses
    @Operation(summary = "특정 멤버가 참여한 채팅방 전체 목록 조회",description = "")
    public ApiResponse<ChatRoomResponseDto.ChatRoomItemListDto> getChatRoomItemList(){
        //TODO : 채팅방 목록 전체 조회
        return null;
    }

    @GetMapping("/ongoing")
    @CommonApiResponses
    @Operation(summary = "특정 멤버가 참여한 채팅방 중 진행중 목록 조회",description = "")
    public ApiResponse<ChatRoomResponseDto.ChatRoomItemListDto> getChatRoomOngoingItemList(){
        //TODO : 채팅방 목록 전체 조회
        List<ChatRoomResponseDto.ChatRoomItemDto> chatRoomItemDtoList = chatRoomQueryService.findAllChatRooms();
        return ApiResponse.onSuccess(ChatRoomConverter.toChatRoomItemListDto(chatRoomItemDtoList));
    }

    @GetMapping("/confirmed")
    @CommonApiResponses
    @Operation(summary = "특정 멤버가 참여한 채팅방 중 동행확정 목록 조회",description = "")
    public ApiResponse<ChatRoomResponseDto.ChatRoomItemListDto> getChatRoomConfirmedItemList(){
        //TODO : 채팅방 목록 전체 조회
        return null;
    }

    @GetMapping("/terminated")
    @CommonApiResponses
    @Operation(summary = "특정 멤버가 참여한 채팅방 중 종료 목록 조회",description = "")
    public ApiResponse<ChatRoomResponseDto.ChatRoomItemListDto> getChatRoomTerminatedItemList(){
        //TODO : 채팅방 목록 전체 조회
        return null;
    }
}
