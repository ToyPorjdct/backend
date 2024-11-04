package com.toyproject.project.domain.board.repository;

import com.toyproject.project.domain.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByIdAndMemberId(Long boardId, Long id);
}
