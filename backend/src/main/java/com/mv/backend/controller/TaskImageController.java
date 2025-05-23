package com.mv.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mv.backend.entity.TaskImage;
import com.mv.backend.exception.NotFoundException;
import com.mv.backend.repository.TaskImageRepository;

@RestController
@RequestMapping("/api/task-images")
public class TaskImageController {

    private final TaskImageRepository taskImageRepository;

    public TaskImageController(TaskImageRepository taskImageRepository) {
        this.taskImageRepository = taskImageRepository;
    }

    @GetMapping
    public ResponseEntity<List<TaskImage>> getAllTaskImages() {
        return ResponseEntity.ok(taskImageRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskImage> getTaskImageById(@PathVariable Long id) {
        TaskImage image = taskImageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("TaskImage not found with id " + id));
        return ResponseEntity.ok(image);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<TaskImage>> getTaskImagesByTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskImageRepository.findByTaskId(taskId));
    }

    @PostMapping
    public ResponseEntity<TaskImage> createTaskImage(@RequestBody TaskImage image) {
        TaskImage saved = taskImageRepository.save(image);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskImage> updateTaskImage(@PathVariable Long id, @RequestBody TaskImage details) {
        TaskImage image = taskImageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("TaskImage not found with id " + id));
        image.setImageUrl(details.getImageUrl());
        image.setTask(details.getTask());
        TaskImage updated = taskImageRepository.save(image);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskImage(@PathVariable Long id) {
        if (!taskImageRepository.existsById(id)) {
            throw new NotFoundException("TaskImage not found with id " + id);
        }
        taskImageRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
