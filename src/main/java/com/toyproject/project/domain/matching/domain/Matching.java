package com.toyproject.project.domain.matching.domain;

import com.toyproject.project.domain.board.domain.Board;
import com.toyproject.project.domain.matching.domain.status.MatchingStatus;
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
public class Matching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matching_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MatchingStatus status = MatchingStatus.PENDING;


    public void changeStatus(MatchingStatus status) {
        this.status = status;
    }
}
