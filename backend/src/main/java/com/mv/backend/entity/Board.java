package com.mv.backend.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "boards")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    // Relation Board -> User (propriétaire)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    // Relation Board -> List (colonnes du tableau)
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ListColumn> lists; // On va créer ListColumn ensuite

    // Constructeurs
    public Board() {}

    public Board(String name, User owner) {
        this.name = name;
        this.owner = owner;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
    public List<ListColumn> getLists() { return lists; }
    public void setLists(List<ListColumn> lists) { this.lists = lists; }
}
