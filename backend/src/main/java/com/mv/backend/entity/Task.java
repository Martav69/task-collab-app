package com.mv.backend.entity;

import jakarta.persistence.*;
import java.util.List;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 1000)
    private String description;

    // Plusieurs tâches dans une colonne
    @ManyToOne
    @JoinColumn(name = "list_column_id", nullable = false)
    private ListColumn listColumn;

    // Timestamps utiles (optionnel)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Une tâche peut avoir plusieurs images
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskImage> images;

    // Constructeurs
    public Task() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Task(String title, String description, ListColumn listColumn) {
        this.title = title;
        this.description = description;
        this.listColumn = listColumn;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public ListColumn getListColumn() { return listColumn; }
    public void setListColumn(ListColumn listColumn) { this.listColumn = listColumn; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<TaskImage> getImages() { return images; }
    public void setImages(List<TaskImage> images) { this.images = images; }
}
