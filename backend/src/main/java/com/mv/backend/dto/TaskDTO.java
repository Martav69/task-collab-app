package com.mv.backend.dto;

public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    // Bonus possible : date, statut, etc.

    public TaskDTO() {}

    public TaskDTO(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
