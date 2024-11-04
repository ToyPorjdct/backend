package com.toyproject.project.domain.participant.service;

import com.toyproject.project.domain.board.domain.Board;
import com.toyproject.project.domain.participant.domain.Participant;
import com.toyproject.project.domain.board.repository.BoardRepository;
import com.toyproject.project.domain.participant.dto.ParticipantResponseDto;
import com.toyproject.project.domain.participant.repository.ParticipantRepository;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.toyproject.project.domain.participant.domain.status.ParticipationStatus.PENDING;
import static com.toyproject.project.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final BoardRepository boardRepository;

    /**
     * 동행 참가 신청
     */
    @Transactional
    public Long applyParticipant(Long boardId, Member member) {
        Board board = boardRepository.findById(boardId).orElseThrow(()
                -> new CustomException(NOT_FOUND_BOARD));

        if(isAuthor(member, board)){
            throw new CustomException(AUTHOR_CANNOT_APPLY);
        }

        if (isAlreadyParticipant(member, board)) {
            throw new CustomException(ALREADY_EXIST_PARTICIPANT);
        }

        Participant saveParticipant = participantRepository.save(
                Participant.builder()
                        .board(board)
                        .member(member)
                        .build());

        return saveParticipant.getId();
    }

    private static boolean isAuthor(Member member, Board board) {
        return board.getMember().getId().equals(member.getId());
    }

    private boolean isAlreadyParticipant(Member member, Board board) {
        return participantRepository.existsByBoardAndMember(board, member);
    }

    /**
     * 동행 신청자 조회
     */
    public List<ParticipantResponseDto> getParticipant(Long boardId, Member member) {
        boardRepository.findByIdAndMemberId(boardId, member.getId())
                .orElseThrow(() -> new CustomException(NO_AUTHORITY));

        List<Participant> participantList = participantRepository.findByBoardIdAndStatus(boardId, PENDING);
        return participantList.stream()
                .map(ParticipantResponseDto::from)
                .collect(Collectors.toList());

    }
}
