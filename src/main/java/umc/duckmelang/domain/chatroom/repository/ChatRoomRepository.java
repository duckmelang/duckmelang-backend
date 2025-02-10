package umc.duckmelang.domain.chatroom.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import umc.duckmelang.domain.chatroom.domain.ChatRoom;
import umc.duckmelang.domain.member.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // 채팅 전송 시 채팅방이 존재하는지 확인하기 위해 사용한다.
    Optional<ChatRoom> findByPostIdAndOtherMemberId(Long postId, Long otherMemberId);

    @Query("SELECT cr FROM ChatRoom cr " +
            "JOIN FETCH cr.post p " +
            "JOIN FETCH cr.otherMember om " +
            "JOIN FETCH p.member pm " +
            "WHERE cr.post.member = :member " +
            "OR cr.otherMember = :member")
    List<ChatRoom> findAllByMemberWithPostAndCounterpart(@Param("member") Member member,
                                                         Pageable pageable);
}