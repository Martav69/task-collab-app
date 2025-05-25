package com.mv.backend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
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

import com.mv.backend.dto.TaskDTO;
import com.mv.backend.entity.Task;
import com.mv.backend.entity.TaskImage;
import com.mv.backend.exception.NotFoundException;
import com.mv.backend.repository.TaskImageRepository;
import com.mv.backend.repository.TaskRepository;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository taskRepository;
    private final TaskImageRepository taskImageRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public TaskController(TaskRepository taskRepository, TaskImageRepository taskImageRepository) {
        this.taskRepository = taskRepository;
        this.taskImageRepository = taskImageRepository;
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        List<TaskDTO> dtos = tasks.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Task not found with id " + id));
        return ResponseEntity.ok(toDTO(task));
    }

    // GET by ListColumn
    @GetMapping("/list-column/{listColumnId}")
    public ResponseEntity<List<TaskDTO>> getTasksByListColumn(@PathVariable Long listColumnId) {
        List<Task> tasks = taskRepository.findByListColumnId(listColumnId);
        List<TaskDTO> dtos = tasks.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // POST
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody Task task) {
        Task saved = taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody Task details) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Task not found with id " + id));
        task.setTitle(details.getTitle());
        task.setDescription(details.getDescription());
        task.setListColumn(details.getListColumn());
        task.setCreatedAt(details.getCreatedAt());
        task.setUpdatedAt(details.getUpdatedAt());
        Task updated = taskRepository.save(task);
        return ResponseEntity.ok(toDTO(updated));
    }

    // DELETE (avec suppression image physique)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (!taskRepository.existsById(id)) {
            throw new NotFoundException("Task not found with id " + id);
        }

        List<TaskImage> images = taskImageRepository.findByTaskId(id);
        for (TaskImage img : images) {
            try {
                String imageUrl = img.getImageUrl();
                if (imageUrl != null && imageUrl.startsWith("/api/task-images/file/")) {
                    String filename = imageUrl.replace("/api/task-images/file/", "");
                    Path path = Paths.get(uploadDir, filename);
                    Files.deleteIfExists(path);
                }
            } catch (IOException e) {
                System.err.println("Erreur suppression fichier image: " + e.getMessage());
            }
            taskImageRepository.delete(img);
        }
        taskRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    // --- MAPPING ENTITE -> DTO ---
    private TaskDTO toDTO(Task entity) {
        return new TaskDTO(
            entity.getId(),
            entity.getTitle(),
            entity.getDescription()
        );
    }
}
