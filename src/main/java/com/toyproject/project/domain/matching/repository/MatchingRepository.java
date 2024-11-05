package com.toyproject.project.domain.matching.repository;

import com.toyproject.project.domain.board.domain.Board;
import com.toyproject.project.domain.matching.domain.Matching;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.domain.matching.domain.status.MatchingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchingRepository extends JpaRepository<Matching, Long>{
    boolean existsByBoardAndMember(Board board, Member member);
    List<Matching> findByBoardIdAndStatus(Long boardId, MatchingStatus matchingStatus);

    void deleteByBoardId(Long boardId);
}
