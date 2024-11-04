package com.toyproject.project.domain.participant.repository;

import com.toyproject.project.domain.board.domain.Board;
import com.toyproject.project.domain.participant.domain.Participant;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.domain.participant.domain.status.ParticipationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long>{
    boolean existsByBoardAndMember(Board board, Member member);

    List<Participant> findByBoardIdAndStatus(Long boardId, ParticipationStatus participationStatus);
}
