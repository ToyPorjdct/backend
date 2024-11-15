package com.toyproject.project.domain.board.domain;

import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.global.entity.BaseEntity;
import com.toyproject.project.global.exception.CustomException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.toyproject.project.global.exception.ErrorCode.EXCEED_MAX_PARTICIPANT;

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
    private String destination;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer maxParticipant;
    private Integer viewCount;
    private Integer likesCount;
    private boolean isClosed;

    @Builder.Default
    private Integer currentParticipant = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public void increaseCurrentParticipant() {
        if (currentParticipant >= maxParticipant) {
            throw new CustomException(EXCEED_MAX_PARTICIPANT);
        }
        currentParticipant++;
    }

}
