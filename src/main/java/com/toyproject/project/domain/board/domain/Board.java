package com.toyproject.project.domain.board.domain;

import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer maxParticipant;

    @Builder.Default
    private Integer currentParticipant = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

}
