package com.mv.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mv.backend.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByListColumnId(Long listColumnId);
}
