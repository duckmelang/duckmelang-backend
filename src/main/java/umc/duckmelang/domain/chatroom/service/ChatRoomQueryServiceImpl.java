package umc.duckmelang.domain.chatroom.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.chatroom.domain.ChatRoom;
import umc.duckmelang.domain.chatroom.domain.enums.ChatRoomStatus;
import umc.duckmelang.domain.chatroom.dto.ChatRoomResponseDto;
import umc.duckmelang.domain.chatroom.repository.ChatRoomRepository;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.repository.MemberProfileImageRepository;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.repository.PostRepository;
import umc.duckmelang.domain.postimage.domain.PostImage;
import umc.duckmelang.domain.member.repository.MemberRepository;
import umc.duckmelang.domain.postimage.repository.PostImageRepository;
import umc.duckmelang.mongo.chatmessage.dto.ChatMessageResponseDto;
import umc.duckmelang.mongo.chatmessage.service.ChatMessageQueryService;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomQueryServiceImpl implements ChatRoomQueryService {
    private final ChatRoomRepository chatRoomRepository;
    private final PostImageRepository postImageRepository;
    private final MemberRepository memberRepository;
    private final MemberProfileImageRepository memberProfileImageRepository;
    private final ChatMessageQueryService chatMessageService;
    private final PostRepository postRepository;

    @Value("${spring.custom.default.profile-image}")
    private String defaultImage;

    @Override
    public Page<ChatRoomResponseDto.ChatRoomItemDto> findAllChatRooms(Long memberId, int page) {
        Pageable pageable = PageRequest.of(page,20);
        Page<ChatRoom> chatRooms = chatRoomRepository.findAllByMemberWithPostAndCounterpart(memberId, pageable);

        List<ChatRoomResponseDto.ChatRoomItemDto> chatRoomItemDtos = getChatRoomItemDtoList(chatRooms, memberId);

        return new PageImpl<>(chatRoomItemDtos, pageable, chatRooms.getTotalElements());
    }

    @Override
    public Page<ChatRoomResponseDto.ChatRoomItemDto> findOngoingChatRooms(Long memberId, int page) {
        Pageable pageable = PageRequest.of(page,20);
        Page<ChatRoom> chatRooms = chatRoomRepository.findOngoingByMemberId(memberId, pageable);

        List<ChatRoomResponseDto.ChatRoomItemDto> chatRoomItemDtos = getChatRoomItemDtoList(chatRooms, memberId);

        return new PageImpl<>(chatRoomItemDtos, pageable, chatRooms.getTotalElements());
    }

    @Override
    public Page<ChatRoomResponseDto.ChatRoomItemDto> findConfirmedChatRooms(Long memberId, int page) {
        Pageable pageable = PageRequest.of(page,20);
        Page<ChatRoom> chatRooms = chatRoomRepository.findConfirmedByMemberId(memberId, pageable);
        List<ChatRoomResponseDto.ChatRoomItemDto> chatRoomItemDtos = getChatRoomItemDtoList(chatRooms, memberId);

        return new PageImpl<>(chatRoomItemDtos, pageable, chatRooms.getTotalElements());
    }

    @Override
    public Page<ChatRoomResponseDto.ChatRoomItemDto> findTerminatedChatRooms(Long memberId, int page) {
        Pageable pageable = PageRequest.of(page,20);
        Page<ChatRoom> chatRooms = chatRoomRepository.findTerminatedByMemberId(memberId, pageable);
        List<ChatRoomResponseDto.ChatRoomItemDto> chatRoomItemDtos = getChatRoomItemDtoList(chatRooms, memberId);

        return new PageImpl<>(chatRoomItemDtos, pageable, chatRooms.getTotalElements());
    }

    @Override
    public ChatRoomResponseDto.ChatRoomDetailDto findChatRoomDetail(Long memberId, Long chatRoomId) {
        return null;
    }

    public List<ChatRoomResponseDto.ChatRoomItemDto> getChatRoomItemDtoList(Page<ChatRoom> chatRooms, Long memberId){

        // 채팅방 ID 리스트 추출
        List<Long> chatRoomIds = chatRooms.stream()
                .map(ChatRoom::getId)
                .collect(Collectors.toList());

        // 한 번의 쿼리로 모든 채팅방의 최신 메시지 조회
        Map<Long, ChatMessageResponseDto.LatestChatMessageDto> latestMessagesMap =
                chatMessageService.getLatestMessagesByChatRoomIds(chatRoomIds);


        return chatRooms
                .stream().sorted((chatRoom1, chatRoom2) -> {
                    LocalDateTime priority1 = Optional.ofNullable(latestMessagesMap.get(chatRoom1.getId()))
                            .map(ChatMessageResponseDto.LatestChatMessageDto::getCreatedAt)
                            .orElse(LocalDateTime.MAX);
                    LocalDateTime priority2 = Optional.ofNullable(latestMessagesMap.get(chatRoom2.getId()))
                            .map(ChatMessageResponseDto.LatestChatMessageDto::getCreatedAt)
                            .orElse(LocalDateTime.MAX);
                    return priority1.compareTo(priority2);
                })
                .map(chatRoom -> {
                    // 현재 회원이 게시글 작성자인지 확인
                    boolean isPostWriter = chatRoom.getPost().getMember().getId().equals(memberId);

                    // 상대방 정보 설정
                    Member otherMember = isPostWriter ? chatRoom.getOtherMember() : chatRoom.getPost().getMember();

                    // 게시글의 최신 이미지 조회
                    String latestPostImageUrl = postImageRepository
                            .findFirstByPostIdOrderByCreatedAtAsc(chatRoom.getPost().getId())
                            .map(PostImage::getPostImageUrl)
                            .orElse(defaultImage);

                    // 상대방의 최신 프로필 이미지 조회
                    String latestProfileImageUrl = memberProfileImageRepository
                            .findFirstByMemberIdAndIsPublicTrueOrderByCreatedAtAsc(otherMember.getId())
                            .map(MemberProfileImage::getMemberImage)
                            .orElse(defaultImage);

                    //post eventdate 지났는지 확인하고 chatroom status 변경
                    chatRoom.hasTerminated();

                    // Map에서 해당 채팅방의 최신 메시지 조회
                    ChatMessageResponseDto.LatestChatMessageDto latestMessage = latestMessagesMap.get(chatRoom.getId());

                    return ChatRoomResponseDto.ChatRoomItemDto.builder()
                            .chatRoomId(chatRoom.getId())
                            .postId(chatRoom.getPost().getId())
                            .postTitle(chatRoom.getPost().getTitle())
                            .postImage(latestPostImageUrl)
                            .oppositeId(otherMember.getId())
                            .oppositeNickname(otherMember.getNickname())
                            .oppositeProfileImage(latestProfileImageUrl)
                            .status(chatRoom.getChatRoomStatus().toString())
                            .lastMessage(latestMessage != null ? latestMessage.getContent() : null)
                            .lastMessageTime(latestMessage != null ? latestMessage.getCreatedAt() : null)
                            .build();
                })
                .collect(Collectors.toList());

    }

}
