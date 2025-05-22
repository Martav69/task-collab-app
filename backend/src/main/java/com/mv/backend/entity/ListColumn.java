package com.mv.backend.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "list_columns")
public class ListColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    // Plusieurs colonnes pour un board
    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    // Une colonne contient plusieurs tâches
    @OneToMany(mappedBy = "listColumn", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

    // Constructeurs
    public ListColumn() {}

    public ListColumn(String name, Board board) {
        this.name = name;
        this.board = board;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Board getBoard() { return board; }
    public void setBoard(Board board) { this.board = board; }
    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }
}
