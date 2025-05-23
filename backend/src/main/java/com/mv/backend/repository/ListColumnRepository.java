package com.mv.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mv.backend.entity.ListColumn;

public interface ListColumnRepository extends JpaRepository<ListColumn, Long> {
    List<ListColumn> findByBoardId(Long boardId);
}
