package com.toyproject.project.domain.matching.service;

import com.toyproject.project.domain.board.domain.Board;
import com.toyproject.project.domain.matching.domain.Matching;
import com.toyproject.project.domain.board.repository.BoardRepository;
import com.toyproject.project.domain.matching.dto.MatchingResponseDto;
import com.toyproject.project.domain.matching.repository.MatchingRepository;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.toyproject.project.domain.matching.domain.status.MatchingStatus.APPROVED;
import static com.toyproject.project.domain.matching.domain.status.MatchingStatus.PENDING;
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

    private static boolean isAuthor(Member member, Board board) {
        return board.getMember().getId().equals(member.getId());
    }

    private boolean isAlreadyMatching(Member member, Board board) {
        return matchingRepository.existsByBoardAndMember(board, member);
    }

    /**
     * 동행 신청자 조회
     */
    public List<MatchingResponseDto> getMatchingList(Long boardId, Member member) {
        boardRepository.findByIdAndMemberId(boardId, member.getId())
                .orElseThrow(() -> new CustomException(NO_AUTHORITY));

        List<Matching> matchingList = matchingRepository.findByBoardIdAndStatus(boardId, PENDING);
        return matchingList.stream()
                .map(MatchingResponseDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 동행 신청 승인
     */
    @Transactional
    public void acceptMatching(Long matchingId, Member member) {
        Matching matching = matchingRepository.findById(matchingId).orElseThrow();
        matching.changeStatus(APPROVED);
    }

}
