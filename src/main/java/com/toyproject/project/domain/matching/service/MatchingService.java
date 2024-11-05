package com.toyproject.project.domain.matching.service;

import com.toyproject.project.domain.board.domain.Board;
import com.toyproject.project.domain.matching.domain.Matching;
import com.toyproject.project.domain.board.repository.BoardRepository;
import com.toyproject.project.domain.matching.repository.MatchingRepository;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static com.toyproject.project.domain.matching.domain.status.MatchingStatus.*;
import static com.toyproject.project.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MatchingService {

    private final MatchingRepository matchingRepository;
    private final BoardRepository boardRepository;

    /**
     * 동행 참가 신청
     */
    @Transactional
    public Long createMatching(Long boardId, Member member) {
        Board board = boardRepository.findById(boardId).orElseThrow(()
                -> new CustomException(NOT_FOUND_BOARD));

        if(isAuthor(member, board)){
            throw new CustomException(AUTHOR_CANNOT_APPLY);
        }

        if (isAlreadyMatching(member, board)) {
            throw new CustomException(ALREADY_EXIST_PARTICIPANT);
        }

        Matching saveMatching = matchingRepository.save(
                Matching.builder()
                        .board(board)
                        .member(member)
                        .build());

        return saveMatching.getId();
    }


    /**
     * 동행 신청 승인
     */
    @Transactional
    public void acceptMatching(Long matchingId) {
        Matching matching = matchingRepository.findById(matchingId).orElseThrow();
        matching.changeStatus(APPROVED);
        matching.getBoard().increaseCurrentParticipant();
    }

    /**
     * 동행 신청 거절
     */
    @Transactional
    public void rejectMatching(Long matchingId) {
        Matching matching = matchingRepository.findById(matchingId).orElseThrow();
        matching.changeStatus(REJECTED);
    }


    private static boolean isAuthor(Member member, Board board) {
        return board.getMember() == member;
    }

    private boolean isAlreadyMatching(Member member, Board board) {
        return matchingRepository.existsByBoardAndMember(board, member);
    }
}
