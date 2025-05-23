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

import com.mv.backend.entity.Board;
import com.mv.backend.exception.NotFoundException;
import com.mv.backend.repository.BoardRepository;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardRepository boardRepository;

    public BoardController(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @GetMapping
    public ResponseEntity<List<Board>> getAllBoards() {
        List<Board> boards = boardRepository.findAll();
        return ResponseEntity.ok(boards);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> getBoardById(@PathVariable Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Board not found with id " + id));
        return ResponseEntity.ok(board);
    }

    @PostMapping
    public ResponseEntity<Board> createBoard(@RequestBody Board board) {
        Board savedBoard = boardRepository.save(board);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBoard);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Board> updateBoard(@PathVariable Long id, @RequestBody Board boardDetails) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Board not found with id " + id));
        board.setName(boardDetails.getName());
        board.setOwner(boardDetails.getOwner()); // Si on veux changer le propriétaire 
        Board updatedBoard = boardRepository.save(board);
        return ResponseEntity.ok(updatedBoard);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        if (!boardRepository.existsById(id)) {
            throw new NotFoundException("Board not found with id " + id);
        }
        boardRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // (Optionnel) GET by owner
    @GetMapping("/user/{ownerId}")
    public ResponseEntity<List<Board>> getBoardsByOwner(@PathVariable Long ownerId) {
        List<Board> boards = boardRepository.findByOwnerId(ownerId);
        return ResponseEntity.ok(boards);
    }
}
