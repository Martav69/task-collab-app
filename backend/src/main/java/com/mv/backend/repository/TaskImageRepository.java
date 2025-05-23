package com.mv.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mv.backend.entity.TaskImage;

public interface TaskImageRepository extends JpaRepository<TaskImage, Long> {
    List<TaskImage> findByTaskId(Long taskId);
}
