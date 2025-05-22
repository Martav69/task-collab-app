package com.mv.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "task_images")
public class TaskImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl; // Chemin ou URL du fichier sur le serveur

    // Une image appartient à une tâche
    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    // Constructeurs
    public TaskImage() {}

    public TaskImage(String imageUrl, Task task) {
        this.imageUrl = imageUrl;
        this.task = task;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Task getTask() { return task; }
    public void setTask(Task task) { this.task = task; }
}
