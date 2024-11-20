package com.toyproject.project.domain.board.repository;

import com.toyproject.project.domain.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT b FROM Board b JOIN FETCH b.member m")
    List<Board> findAllWithMember();

    @Query("SELECT b FROM Board b JOIN FETCH b.member m WHERE b.id = :id")
    Optional<Board> findByIdWithMember(Long id);
}
