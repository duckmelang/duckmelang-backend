package umc.duckmelang.domain.chatroom.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import umc.duckmelang.domain.chatroom.converter.ChatRoomConverter;
import umc.duckmelang.domain.chatroom.dto.ChatRoomResponseDto;
import umc.duckmelang.domain.chatroom.service.ChatRoomQueryService;
import umc.duckmelang.global.annotations.CommonApiResponses;
import umc.duckmelang.global.apipayload.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/chatrooms")
@RequiredArgsConstructor
public class ChatRoomRestController {
    private final ChatRoomQueryService chatRoomQueryService;

    @GetMapping("")
    @CommonApiResponses
    @Operation(summary = "특정 멤버가 참여한 채팅방 전체 목록 조회",description = "memberId는 임시로 사용하고 추후 jwt로 교체합니다. \n 이 api의 response로 넘기는 chatRoomId를 채팅방 세부 내역 조회 api의 request로 주시면 됩니다.\n status는 채팅방의 상태를 의미합니다")
    public ApiResponse<ChatRoomResponseDto.ChatRoomItemListDto> getChatRoomItemList(@RequestParam Long memberId,  // 임시로 사용. 나중에 JWT에서 추출할 예정
                                                                                    @RequestParam int page){
                                                                                    //TODO : 채팅방 목록 전체 조회
        Page<ChatRoomResponseDto.ChatRoomItemDto> chatRoomItemDtoList = chatRoomQueryService.findAllChatRooms(memberId, page);
        return ApiResponse.onSuccess(ChatRoomConverter.toChatRoomItemListDto(chatRoomItemDtoList));
    }

    @GetMapping("/ongoing")
    @CommonApiResponses
    @Operation(summary = "특정 멤버가 참여한 채팅방 중 진행중 목록 조회",description = "memberId는 임시로 사용하고 추후 jwt로 교체합니다. \n 이 api의 response로 넘기는 chatRoomId를 채팅방 세부 내역 조회 api의 request로 주시면 됩니다.")
    public ApiResponse<ChatRoomResponseDto.ChatRoomItemListDto> getChatRoomOngoingItemList(@RequestParam Long memberId,  // 임시로 사용. 나중에 JWT에서 추출할 예정
                                                                                           @RequestParam int page){
        //TODO : 채팅방 목록 전체 조회
        Page<ChatRoomResponseDto.ChatRoomItemDto> chatRoomItemDtoList = chatRoomQueryService.findOngoingChatRooms(memberId, page);
        return ApiResponse.onSuccess(ChatRoomConverter.toChatRoomItemListDto(chatRoomItemDtoList));
    }

    @GetMapping("/confirmed")
    @CommonApiResponses
    @Operation(summary = "특정 멤버가 참여한 채팅방 중 동행확정 목록 조회",description = "memberId는 임시로 사용하고 추후 jwt로 교체합니다. \n 이 api의 response로 넘기는 chatRoomId를 채팅방 세부 내역 조회 api의 request로 주시면 됩니다.")
    public ApiResponse<ChatRoomResponseDto.ChatRoomItemListDto> getChatRoomConfirmedItemList(@RequestParam Long memberId,  // 임시로 사용. 나중에 JWT에서 추출할 예정
                                                                                             @RequestParam int page){
        //TODO : 채팅방 목록 전체 조회
        Page<ChatRoomResponseDto.ChatRoomItemDto> chatRoomItemDtoList = chatRoomQueryService.findConfirmedChatRooms(memberId, page);
        return ApiResponse.onSuccess(ChatRoomConverter.toChatRoomItemListDto(chatRoomItemDtoList));
    }

    @GetMapping("/terminated")
    @CommonApiResponses
    @Operation(summary = "특정 멤버가 참여한 채팅방 중 종료 목록 조회",description = "memberId는 임시로 사용하고 추후 jwt로 교체합니다. \n 이 api의 response로 넘기는 chatRoomId를 채팅방 세부 내역 조회 api의 request로 주시면 됩니다.")
    public ApiResponse<ChatRoomResponseDto.ChatRoomItemListDto> getChatRoomTerminatedItemList(@RequestParam Long memberId,  // 임시로 사용. 나중에 JWT에서 추출할 예정
                                                                                              @RequestParam int page){
        //TODO : 채팅방 목록 전체 조회
        Page<ChatRoomResponseDto.ChatRoomItemDto> chatRoomItemDtoList = chatRoomQueryService.findTerminatedChatRooms(memberId, page);
        return ApiResponse.onSuccess(ChatRoomConverter.toChatRoomItemListDto(chatRoomItemDtoList));
    }
}
