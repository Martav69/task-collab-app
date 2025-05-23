package com.mv.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mv.backend.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByOwnerId(Long ownerId);
}
