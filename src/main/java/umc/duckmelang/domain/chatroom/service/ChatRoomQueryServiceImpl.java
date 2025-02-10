package umc.duckmelang.domain.chatroom.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.chatroom.converter.ChatRoomConverter;
import umc.duckmelang.domain.chatroom.domain.ChatRoom;
import umc.duckmelang.domain.chatroom.dto.ChatRoomResponseDto;
import umc.duckmelang.domain.chatroom.repository.ChatRoomRepository;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.repository.MemberRepository;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.MemberException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomQueryServiceImpl implements ChatRoomQueryService {
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<ChatRoomResponseDto.ChatRoomItemDto> findAllChatRooms(Long memberId, int page) {
        Member currentMember = memberRepository.findById(memberId).orElseThrow(()-> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        List<ChatRoom> chatRooms = chatRoomRepository.findAllByMemberWithPostAndCounterpart(currentMember, PageRequest.of(page, 20));


        return chatRooms.stream()
                .map(chatRoom -> {
                    boolean isPostWriter = chatRoom.getPost().getMember().equals(currentMember);
                    Member counterpart = isPostWriter ?
                            chatRoom.getOtherMember() :
                            chatRoom.getPost().getMember();

                    return ChatRoomConverter.toChatRoomItemDto(chatRoom, counterpart, chatRoom.getPost());
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatRoomResponseDto.ChatRoomItemDto> findOngoingChatRooms(Long memberId) {
        return List.of();
    }

    @Override
    public List<ChatRoomResponseDto.ChatRoomItemDto> findConfirmedChatRooms(Long memberId) {
        return List.of();
    }

    @Override
    public List<ChatRoomResponseDto.ChatRoomItemDto> findTerminatedChatRooms(Long memberId) {
        return List.of();
    }
}
