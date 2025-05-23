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

import com.mv.backend.entity.Task;
import com.mv.backend.exception.NotFoundException;
import com.mv.backend.repository.TaskRepository;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found with id " + id));
        return ResponseEntity.ok(task);
    }

    @GetMapping("/list-column/{listColumnId}")
    public ResponseEntity<List<Task>> getTasksByListColumn(@PathVariable Long listColumnId) {
        return ResponseEntity.ok(taskRepository.findByListColumnId(listColumnId));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task saved = taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task details) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found with id " + id));
        task.setTitle(details.getTitle());
        task.setDescription(details.getDescription());
        task.setListColumn(details.getListColumn());
        task.setCreatedAt(details.getCreatedAt());
        task.setUpdatedAt(details.getUpdatedAt());
        Task updated = taskRepository.save(task);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (!taskRepository.existsById(id)) {
            throw new NotFoundException("Task not found with id " + id);
        }
        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
