package com.toyproject.project.domain.board.repository;

import com.toyproject.project.domain.board.domain.Board;
import com.toyproject.project.domain.board.domain.Likes;
import com.toyproject.project.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    boolean existsByBoardAndMember(Board board, Member member);
    Optional<Likes> findByBoardAndMember(Board board, Member member);

    @Query("""
           select l 
           from Likes l 
           join fetch l.board b 
           join fetch b.member m 
           where l.member = :member
           """)
    List<Likes> findByMemberWithBoard(Member member);
}
