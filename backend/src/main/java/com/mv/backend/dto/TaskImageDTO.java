package com.mv.backend.dto;

public class TaskImageDTO {
    private Long id;
    private String imageUrl;
    private Long taskId;

    public TaskImageDTO() {}

    public TaskImageDTO(Long id, String imageUrl, Long taskId) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.taskId = taskId;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
}
