package com.mv.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

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

import com.mv.backend.dto.ListColumnDTO;
import com.mv.backend.dto.TaskDTO;
import com.mv.backend.entity.ListColumn;
import com.mv.backend.entity.Task;
import com.mv.backend.exception.NotFoundException;
import com.mv.backend.repository.ListColumnRepository;

@RestController
@RequestMapping("/api/list-columns")
public class ListColumnController {

    private final ListColumnRepository listColumnRepository;

    public ListColumnController(ListColumnRepository listColumnRepository) {
        this.listColumnRepository = listColumnRepository;
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<ListColumnDTO>> getAllListColumns() {
        List<ListColumn> columns = listColumnRepository.findAll();
        List<ListColumnDTO> dtos = columns.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ListColumnDTO> getListColumnById(@PathVariable Long id) {
        ListColumn col = listColumnRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("ListColumn not found with id " + id));
        return ResponseEntity.ok(toDTO(col));
    }

    // GET BY BOARD
    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<ListColumnDTO>> getListColumnsByBoard(@PathVariable Long boardId) {
        List<ListColumn> columns = listColumnRepository.findByBoardId(boardId);
        List<ListColumnDTO> dtos = columns.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // POST
    @PostMapping
    public ResponseEntity<ListColumnDTO> createListColumn(@RequestBody ListColumn listColumn) {
        ListColumn saved = listColumnRepository.save(listColumn);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<ListColumnDTO> updateListColumn(@PathVariable Long id, @RequestBody ListColumn details) {
        ListColumn listColumn = listColumnRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("ListColumn not found with id " + id));
        listColumn.setName(details.getName());
        listColumn.setBoard(details.getBoard());
        ListColumn updated = listColumnRepository.save(listColumn);
        return ResponseEntity.ok(toDTO(updated));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListColumn(@PathVariable Long id) {
        if (!listColumnRepository.existsById(id)) {
            throw new NotFoundException("ListColumn not found with id " + id);
        }
        listColumnRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // --- MAPPING ENTITE -> DTO ---
    private ListColumnDTO toDTO(ListColumn entity) {
        return new ListColumnDTO(
            entity.getId(),
            entity.getName(),
            entity.getBoard() != null ? entity.getBoard().getId() : null,
            entity.getBoard() != null ? entity.getBoard().getName() : null,
            entity.getTasks() != null
                ? entity.getTasks().stream()
                    .map(this::toTaskDTO)
                    .collect(Collectors.toList())
                : null
        );
    }

    private TaskDTO toTaskDTO(Task t) {
        return new TaskDTO(
            t.getId(),
            t.getTitle(),
            t.getDescription(),
            null
        );
    }
}
