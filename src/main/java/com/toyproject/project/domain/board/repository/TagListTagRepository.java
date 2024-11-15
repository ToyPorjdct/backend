package com.toyproject.project.domain.board.repository;

import com.toyproject.project.domain.board.domain.TagList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagListTagRepository extends JpaRepository<TagList, Long> {
}
