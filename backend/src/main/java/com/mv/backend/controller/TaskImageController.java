package com.mv.backend.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mv.backend.dto.TaskImageDTO;
import com.mv.backend.entity.Task;
import com.mv.backend.entity.TaskImage;
import com.mv.backend.repository.TaskImageRepository;
import com.mv.backend.repository.TaskRepository;

@RestController
@RequestMapping("/api/task-images")
public class TaskImageController {

    private final TaskImageRepository taskImageRepository;
    private final TaskRepository taskRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public TaskImageController(TaskImageRepository taskImageRepository, TaskRepository taskRepository) {
        this.taskImageRepository = taskImageRepository;
        this.taskRepository = taskRepository;
    }

    @PostMapping("/upload/{taskId}")
    public ResponseEntity<?> uploadImage(@PathVariable Long taskId, @RequestParam("file") MultipartFile file) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        if (taskOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file provided");
        }

        if (file.getSize() > 5 * 1024 * 1024) { // 5 Mo
            return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body("File too large (max 5MB)");
        }


        // Vérifie le type MIME (officiel) et l'extension (pour + de sécurité)
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        if (contentType == null || originalFilename == null) {
            return ResponseEntity.badRequest().body("Invalid file");
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        boolean isValidType = 
            (contentType.equals("image/png") && extension.equals("png")) ||
            ((contentType.equals("image/jpeg") || contentType.equals("image/jpg")) && (extension.equals("jpg") || extension.equals("jpeg"))) ||
            (contentType.equals("image/webp") && extension.equals("webp"));

        if (!isValidType) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Only PNG, JPG, JPEG, WEBP files are allowed");
        }

        try {
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String filename = System.currentTimeMillis() + "_" + originalFilename;
            Path filePath = Paths.get(uploadDir, filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            TaskImage taskImage = new TaskImage();
            taskImage.setTask(taskOpt.get());
            taskImage.setImageUrl("/api/task-images/file/" + filename);
            taskImageRepository.save(taskImage);

            TaskImageDTO dto = new TaskImageDTO(
                taskImage.getId(),
                taskImage.getImageUrl(),
                taskImage.getTask().getId()
            );

            return ResponseEntity.ok(dto);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save image");
        }
    }


    // Pour récupérer une image par son nom
    @GetMapping("/file/{filename:.+}")
    public ResponseEntity<byte[]> getFile(@PathVariable String filename) throws IOException {
        Path filePath = Paths.get(uploadDir, filename);
        byte[] fileContent = Files.readAllBytes(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // ou IMAGE_PNG selon tes besoins
        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }
}
