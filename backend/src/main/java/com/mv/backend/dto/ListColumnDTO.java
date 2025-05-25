package com.mv.backend.dto;

import java.util.List;

public class ListColumnDTO {
    private Long id;
    private String name;
    private Long boardId;
    private String boardName;
    private List<TaskDTO> tasks;

    public ListColumnDTO() {}

    public ListColumnDTO(Long id, String name, Long boardId, String boardName, List<TaskDTO> tasks) {
        this.id = id;
        this.name = name;
        this.boardId = boardId;
        this.boardName = boardName;
        this.tasks = tasks;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getBoardId() { return boardId; }
    public void setBoardId(Long boardId) { this.boardId = boardId; }

    public String getBoardName() { return boardName; }
    public void setBoardName(String boardName) { this.boardName = boardName; }

    public List<TaskDTO> getTasks() { return tasks; }
    public void setTasks(List<TaskDTO> tasks) { this.tasks = tasks; }
}
