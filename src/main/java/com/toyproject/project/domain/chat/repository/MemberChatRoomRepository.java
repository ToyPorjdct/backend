package com.toyproject.project.domain.chat.repository;

import com.toyproject.project.domain.chat.domain.MemberChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberChatRoomRepository extends JpaRepository<MemberChatRoom, Long>{

    @Query(""" 
            SELECT m 
            FROM MemberChatRoom m 
            JOIN FETCH m.member 
            JOIN FETCH m.chatRoom 
            WHERE m.member.id = :memberId 
            """)
    List<MemberChatRoom> findByMemberId(@Param("memberId") Long memberId);

    Boolean existsByMemberIdAndBoardId(Long memberId, Long boardId);
}
