package com.mv.backend.controller;

import java.util.List;

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

import com.mv.backend.entity.ListColumn;
import com.mv.backend.exception.NotFoundException;
import com.mv.backend.repository.ListColumnRepository;

@RestController
@RequestMapping("/api/list-columns")
public class ListColumnController {

    private final ListColumnRepository listColumnRepository;

    public ListColumnController(ListColumnRepository listColumnRepository) {
        this.listColumnRepository = listColumnRepository;
    }

    @GetMapping
    public ResponseEntity<List<ListColumn>> getAllListColumns() {
        return ResponseEntity.ok(listColumnRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListColumn> getListColumnById(@PathVariable Long id) {
        ListColumn listColumn = listColumnRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ListColumn not found with id " + id));
        return ResponseEntity.ok(listColumn);
    }

    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<ListColumn>> getListColumnsByBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok(listColumnRepository.findByBoardId(boardId));
    }

    @PostMapping
    public ResponseEntity<ListColumn> createListColumn(@RequestBody ListColumn listColumn) {
        ListColumn saved = listColumnRepository.save(listColumn);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListColumn> updateListColumn(@PathVariable Long id, @RequestBody ListColumn details) {
        ListColumn listColumn = listColumnRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ListColumn not found with id " + id));
        listColumn.setName(details.getName());
        listColumn.setBoard(details.getBoard());
        ListColumn updated = listColumnRepository.save(listColumn);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListColumn(@PathVariable Long id) {
        if (!listColumnRepository.existsById(id)) {
            throw new NotFoundException("ListColumn not found with id " + id);
        }
        listColumnRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
