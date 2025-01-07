package com.toyproject.project.domain.board.repository;

import com.toyproject.project.domain.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying // 변경이 일어나는 쿼리와 함께 사용해야 JPA에서 변경 감지와 관련된 처리가 생략됨
    @Query("UPDATE Board b SET b.viewCount = b.viewCount + 1 WHERE b.id = :id")
    void increaseViewCount(Long id);
}
