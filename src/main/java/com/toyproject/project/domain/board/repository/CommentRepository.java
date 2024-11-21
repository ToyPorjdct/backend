package com.toyproject.project.domain.board.repository;

import com.toyproject.project.domain.board.domain.Board;
import com.toyproject.project.domain.board.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("""
            SELECT c 
            FROM Comment c 
            JOIN FETCH c.member
            WHERE c.board.id = :boardId
            """)
    List<Comment> findByBoarIdWithMember(Long boardId);

}
