package com.toyproject.project.domain.board.repository;

import com.toyproject.project.domain.board.domain.Board;
import com.toyproject.project.domain.board.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String tagName);
}
