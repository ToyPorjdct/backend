package com.toyproject.project.domain.board.service;

import com.toyproject.project.domain.board.domain.Board;
import com.toyproject.project.domain.board.domain.Tag;
import com.toyproject.project.domain.board.domain.TagList;
import com.toyproject.project.domain.board.dto.AuthorResponseDto;
import com.toyproject.project.domain.board.dto.BoardCreateRequestDto;
import com.toyproject.project.domain.board.dto.BoardDetailResponseDto;
import com.toyproject.project.domain.board.dto.BoardListResponseDto;
import com.toyproject.project.domain.board.repository.BoardRepository;
import com.toyproject.project.domain.board.repository.TagListRepository;
import com.toyproject.project.domain.board.repository.TagRepository;
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

import static com.toyproject.project.domain.matching.domain.status.MatchingStatus.APPROVED;
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
    private final TagRepository tagRepository;
    private final TagListRepository tagListRepository;

    /**
     * 모집글 작성
     */
    @Transactional
    public Board createBoard(BoardCreateRequestDto boardCreateRequestDto, Member member) {
        Board board = Board.builder()
                .title(boardCreateRequestDto.getTitle())
                .description(boardCreateRequestDto.getDescription())
                .destination(boardCreateRequestDto.getDestination())
                .startDate(boardCreateRequestDto.getStartDate())
                .endDate(boardCreateRequestDto.getEndDate())
                .maxParticipant(boardCreateRequestDto.getMaxParticipant())
                .member(member)
                .build();

        Board savedBoard = boardRepository.save(board);

        // 태그 저장
        for (String tagName : boardCreateRequestDto.getTagNames()) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(new Tag(tagName)));

            TagList tagList = TagList.builder()
                    .board(savedBoard)
                    .tag(tag)
                    .build();

            tagListRepository.save(tagList);
        }


        return savedBoard;
    }

    /**
     * 모집글 삭제
     */
    @Transactional
    public void removeBoard(Long boardId, Member member) {
        Board board = boardRepository.findById(boardId).orElseThrow(()
                -> new CustomException(NOT_FOUND_BOARD));

        if(!isAuthor(member, board)){
            throw new CustomException(NO_AUTHORITY);
        }

        tagListRepository.deleteAllByBoard(board);
        boardRepository.delete(board);
    }

    /**
     * 모집글 상세 조회
     */
    public BoardDetailResponseDto getBoardDetail(Long boardId) {
        Board board = boardRepository.findByIdWithMember(boardId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_BOARD));

        List<String> tagListList = tagListRepository.findByWithTag(board).stream()
                .map(tagList -> tagList.getTag().getName())
                .collect(Collectors.toList());

        AuthorResponseDto author = new AuthorResponseDto(
                board.getMember().getId(),
                board.getMember().getNickname(),
                board.getMember().getProfileImage()
        );

        return BoardDetailResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .startDate(board.getStartDate())
                .endDate(board.getEndDate())
                .description(board.getDescription())
                .destination(board.getDestination())
                .maxParticipant(board.getMaxParticipant())
                .isClosed(board.isClosed())
                .views(board.getViewCount())
                .likes(board.getLikesCount())
                .tags(tagListList)
                .author(author)
                .createdAt(board.getCreatedAt())
                .build();
    }

    /**
     * 모집글 간단 조회
     */
    public List<BoardListResponseDto> getBoardList() {
        List<Board> boards = boardRepository.findAllWithMember();

        return boards.stream()
                .map(board -> {
                    List<String> tagListList = tagListRepository.findByWithTag(board).stream()
                            .map(tagList -> tagList.getTag().getName())
                            .collect(Collectors.toList());

                    return BoardListResponseDto.builder()
                            .id(board.getId())
                            .title(board.getTitle())
                            .startDate(board.getStartDate())
                            .endDate(board.getEndDate())
                            .destination(board.getDestination())
                            .maxParticipant(board.getMaxParticipant())
                            .isClosed(board.isClosed())
                            .views(board.getViewCount())
                            .likes(board.getLikesCount())
                            .tags(tagListList)
                            .author(new AuthorResponseDto(
                                    board.getMember().getId(),
                                    board.getMember().getNickname(),
                                    board.getMember().getProfileImage())
                            )
                            .build();
                })
                .collect(Collectors.toList());
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

    /**
     * 현재 모집글 참가자 조회
     */
    public List<MatchingResponseDto> getCurrentParticipant(Long boardId) {
        List<Matching> matchingList = matchingRepository.findByBoardIdAndStatus(boardId, APPROVED);
        return matchingList.stream()
                .map(MatchingResponseDto::from)
                .collect(Collectors.toList());
    }

    private static boolean isAuthor(Member member, Board board) {
        return board.getMember().equals(member);
    }

}
