package com.toyproject.project.domain.board.service;

import com.toyproject.project.domain.board.domain.Board;
import com.toyproject.project.domain.board.domain.Likes;
import com.toyproject.project.domain.board.dto.AuthorResponseDto;
import com.toyproject.project.domain.board.dto.BoardListResponseDto;
import com.toyproject.project.domain.board.repository.BoardRepository;
import com.toyproject.project.domain.board.repository.LikesRepository;
import com.toyproject.project.domain.board.repository.TagListRepository;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.toyproject.project.global.exception.ErrorCode.DUPLICATE_LIKE;
import static com.toyproject.project.global.exception.ErrorCode.NOT_FOUND_BOARD;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LikesService {

    private final BoardRepository boardRepository;
    private final LikesRepository likesRepository;
    private final TagListRepository tagListRepository;

    @Transactional
    public void addLike(Member member, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_BOARD));

        if (likesRepository.existsByBoardAndMember(board, member)) {
            throw new CustomException(DUPLICATE_LIKE);
        }

        likesRepository.save(Likes.builder()
                .board(board)
                .member(member)
                .build());

        board.updateLikesCount(board.getLikesCount() + 1);
    }

    @Transactional
    public void removeLike(Member member, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_BOARD));

        Likes likes = likesRepository.findByBoardAndMember(board, member)
                        .orElseThrow();

        likesRepository.delete(likes);

        board.updateLikesCount(board.getLikesCount() - 1);
    }

    public List<BoardListResponseDto> getLikeBoardList(Member member) {
        List<Likes> likesList = likesRepository.findByMemberWithBoard(member);

        return likesList.stream()
                .map(likes -> {
                    Board board = likes.getBoard();

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
                                    board.getMember().getNickname(),
                                    board.getMember().getProfileImage()))
                            .build();
                })
                .collect(Collectors.toList());
    }

}
