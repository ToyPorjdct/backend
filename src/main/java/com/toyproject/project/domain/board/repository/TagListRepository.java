package com.toyproject.project.domain.board.repository;

import com.toyproject.project.domain.board.domain.Board;
import com.toyproject.project.domain.board.domain.TagList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TagListRepository extends JpaRepository<TagList, Long> {

    @Query("SELECT t FROM TagList t JOIN FETCH t.tag WHERE t.board = :board")
    List<TagList> findByWithTag(Board board);
}
