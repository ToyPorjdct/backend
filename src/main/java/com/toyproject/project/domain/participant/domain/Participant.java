package com.toyproject.project.domain.participant.domain;

import com.toyproject.project.domain.board.domain.Board;
import com.toyproject.project.domain.participant.domain.status.ParticipationStatus;
import com.toyproject.project.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ParticipationStatus status = ParticipationStatus.PENDING;

}
