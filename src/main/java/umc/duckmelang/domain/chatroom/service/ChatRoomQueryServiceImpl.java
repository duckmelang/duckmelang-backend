package umc.duckmelang.domain.chatroom.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.chatroom.domain.ChatRoom;
import umc.duckmelang.domain.chatroom.dto.ChatRoomResponseDto;
import umc.duckmelang.domain.chatroom.repository.ChatRoomRepository;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.repository.MemberProfileImageRepository;
import umc.duckmelang.domain.postimage.domain.PostImage;
import umc.duckmelang.domain.member.repository.MemberRepository;
import umc.duckmelang.domain.postimage.repository.PostImageRepository;
import umc.duckmelang.mongo.chatmessage.dto.ChatMessageResponseDto;
import umc.duckmelang.mongo.chatmessage.service.ChatMessageQueryService;

import java.util.List;
import java.util.Map;
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

    @Value("${spring.custom.default.profile-image}")
    private String defaultImage;

    @Override
    public Page<ChatRoomResponseDto.ChatRoomItemDto> findAllChatRooms(Long memberId, int page) {
        Page<ChatRoom> chatRooms = chatRoomRepository.findAllByMemberWithPostAndCounterpart(memberId, PageRequest.of(page,20));

        // 채팅방 ID 리스트 추출
        List<Long> chatRoomIds = chatRooms.stream()
                .map(ChatRoom::getId)
                .collect(Collectors.toList());

        // 한 번의 쿼리로 모든 채팅방의 최신 메시지 조회
        Map<Long, ChatMessageResponseDto.LatestChatMessageDto> latestMessagesMap =
                chatMessageService.getLatestMessagesByChatRoomIds(chatRoomIds);


        return chatRooms
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

                    // Map에서 해당 채팅방의 최신 메시지 조회
                    ChatMessageResponseDto.LatestChatMessageDto latestMessage = latestMessagesMap.get(chatRoom.getId());

                    // 현재 회원의 리뷰 완료 여부 확인
                    boolean hasReviewDone = isPostWriter ?
                            chatRoom.isHasReceiverReviewDone() :
                            chatRoom.isHasSenderReviewDone();

                    return ChatRoomResponseDto.ChatRoomItemDto.builder()
                            .chatRoomId(chatRoom.getId())
                            .postId(chatRoom.getPost().getId())
                            .postTitle(chatRoom.getPost().getTitle())
                            .postImage(latestPostImageUrl)
                            .oppositeId(otherMember.getId())
                            .oppositeNickname(otherMember.getNickname())
                            .oppositeProfileImage(latestProfileImageUrl)
                            .lastMessage(latestMessage != null ? latestMessage.getContent() : null)
                            .lastMessageTime(latestMessage != null ? latestMessage.getCreatedAt() : null)
                            .hasMatched(chatRoom.isHasMatched())
                            .hasReviewDone(hasReviewDone)
                            .build();
                });
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
