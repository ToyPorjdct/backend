package com.toyproject.project.domain.board.service;

import com.toyproject.project.domain.board.domain.Board;
import com.toyproject.project.domain.board.dto.BoardCreateRequestDto;
import com.toyproject.project.domain.board.dto.BoardDetailResponseDto;
import com.toyproject.project.domain.board.repository.BoardRepository;
import com.toyproject.project.domain.matching.domain.Matching;
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

import static com.toyproject.project.domain.matching.domain.status.MatchingStatus.PENDING;
import static com.toyproject.project.global.exception.ErrorCode.NOT_FOUND_BOARD;
import static com.toyproject.project.global.exception.ErrorCode.NO_AUTHORITY;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final MatchingRepository matchingRepository;

    /**
     * 모집글 작성
     */
    @Transactional
    public Long createBoard(BoardCreateRequestDto boardCreateRequestDto, Member member) {
        Board board = Board.builder()
                .title(boardCreateRequestDto.getTitle())
                .description(boardCreateRequestDto.getDescription())
                .member(member)
                .maxParticipant(boardCreateRequestDto.getMaxParticipant())
                .startDate(boardCreateRequestDto.getStartDate())
                .endDate(boardCreateRequestDto.getEndDate())
                .build();

        Board savedBoard = boardRepository.save(board);

        return savedBoard.getId();
    }

    /**
     * 모집글 상세 조회
     */
    public BoardDetailResponseDto getDetailBoards(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(()
                -> new CustomException(NOT_FOUND_BOARD));
        return BoardDetailResponseDto.from(board);
    }


    /**
     * 동행 신청자 조회
     */
    public List<MatchingResponseDto> getMatchingList(Long boardId, Member member) {
        Board board = boardRepository.findById(boardId).orElseThrow(()
                -> new CustomException(NOT_FOUND_BOARD));

        if(!isAuthor(member, board)){
            throw new CustomException(NO_AUTHORITY);
        }

        List<Matching> matchingList = matchingRepository.findByBoardIdAndStatus(boardId, PENDING);
        return matchingList.stream()
                .map(MatchingResponseDto::from)
                .collect(Collectors.toList());
    }

    private static boolean isAuthor(Member member, Board board) {
        return board.getMember() == member;
    }
}
