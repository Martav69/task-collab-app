package com.mv.backend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

        // 1. Supprimer d'abord toutes les images liées à cette tâche
        List<TaskImage> images = taskImageRepository.findByTaskId(id);
        /* Debug suppression d'image */
        //System.out.println("Nb images trouvées pour cette task : " + images.size());
        for (TaskImage img : images) {
            // Supprime le fichier physique
            /* Debug suppression d'image */
            // System.out.println("Suppression de l'image : " + img.getImageUrl());
            try {
                String imageUrl = img.getImageUrl();
                if (imageUrl != null && imageUrl.startsWith("/api/task-images/file/")) {
                    String filename = imageUrl.replace("/api/task-images/file/", "");
                    Path path = Paths.get(uploadDir, filename);
                    /* Debug suppression d'image */
                    // System.out.println("Tentative suppression : " + path.toAbsolutePath());
                    Files.deleteIfExists(path);
                }
            } catch (IOException e) {
                // Log si besoin, ne bloque pas la suppression
                System.err.println("Erreur suppression fichier image: " + e.getMessage());
            }
            // Supprime l'entrée en BDD
            taskImageRepository.delete(img);
        }

        // 2. Supprimer la tâche
        taskRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
