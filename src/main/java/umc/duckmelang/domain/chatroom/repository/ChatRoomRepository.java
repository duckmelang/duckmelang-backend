package umc.duckmelang.domain.chatroom.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import umc.duckmelang.domain.chatroom.domain.ChatRoom;
import umc.duckmelang.domain.member.domain.Member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // 채팅 전송 시 채팅방이 존재하는지 확인하기 위해 사용한다.
    Optional<ChatRoom> findByPostIdAndOtherMemberId(Long postId, Long otherMemberId);

    @Query("SELECT DISTINCT cr FROM ChatRoom cr " +
            "LEFT JOIN FETCH cr.post p " +
            "LEFT JOIN FETCH cr.otherMember om " +
            "LEFT JOIN FETCH p.member pm " +
            "WHERE cr.post.member.id = :member OR cr.otherMember.id = :member ")
    Page<ChatRoom> findAllByMemberWithPostAndCounterpart(@Param("member") Long memberId,
                                                         Pageable pageable);

    @Query("SELECT DISTINCT cr FROM ChatRoom cr " +
            "LEFT JOIN FETCH cr.post p " +
            "LEFT JOIN FETCH cr.otherMember om " +
            "LEFT JOIN FETCH p.member pm " +
            "LEFT JOIN Application a ON (a.post = cr.post AND a.member = cr.otherMember) " +
            "WHERE (cr.post.member.id = :member OR cr.otherMember.id = :member) " +
            "AND cr.chatRoomStatus = 'ONGOING' ")
    Page<ChatRoom> findOngoingByMemberId(@Param("member") Long memberId, Pageable pageable);

    @Query("SELECT DISTINCT cr FROM ChatRoom cr " +
            "LEFT JOIN FETCH cr.post p " +
            "LEFT JOIN FETCH cr.otherMember om " +
            "LEFT JOIN FETCH p.member pm " +
            "LEFT JOIN Application a ON (a.post = cr.post AND a.member = cr.otherMember) " +
            "WHERE (cr.post.member.id = :member OR cr.otherMember.id = :member) " +
            "AND cr.chatRoomStatus = 'CONFIRMED' ")
    Page<ChatRoom> findConfirmedByMemberId(@Param("member") Long memberId, Pageable pageable);

    @Query("SELECT DISTINCT cr FROM ChatRoom cr " +
            "LEFT JOIN FETCH cr.post p " +
            "LEFT JOIN FETCH cr.otherMember om " +
            "LEFT JOIN FETCH p.member pm " +
            "LEFT JOIN Application a ON (a.post = cr.post AND a.member = cr.otherMember) " +
            "WHERE (cr.post.member.id = :member OR cr.otherMember.id = :member) " +
            "AND cr.chatRoomStatus = 'TERMINATED' ")
    Page<ChatRoom> findTerminatedByMemberId(@Param("member") Long memberId,Pageable pageable);

}