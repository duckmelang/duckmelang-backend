package umc.duckmelang.domain.chatroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc.duckmelang.domain.chatroom.domain.ChatRoom;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // 채팅 전송 시 채팅방이 존재하는지 확인하기 위해 사용한다.
    Optional<ChatRoom> findByPostIdAndOtherMemberId(Long postId, Long receiverId);
}