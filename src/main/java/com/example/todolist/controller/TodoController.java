package com.example.todolist.controller;

import com.example.todolist.dto.AppUserRecord;
import com.example.todolist.dto.TodoCompleteRecord;
import com.example.todolist.dto.TodoRequestRecord;
import com.example.todolist.dto.TodoUpdateRecord;
import com.example.todolist.service.AppUserService;
import com.example.todolist.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @GetMapping("/todos/{id}")
    public ResponseEntity<TodoRequestRecord> find(@PathVariable Long id) {
        return new ResponseEntity<>(todoService.findById(id),HttpStatus.OK);
    }
    
    @PostMapping("/todos/save")
    public ResponseEntity<TodoRequestRecord> addTodo(@Valid @RequestBody TodoRequestRecord todoRequestRecord, @AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>(todoService.addTodo(todoRequestRecord, userDetails),HttpStatus.OK);
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<TodoUpdateRecord> deleteTodo(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        todoService.deleteTodo(id, userDetails);
        return ResponseEntity.noContent().build();  // 204 No Content
    }

    @PatchMapping("/todos/content/{id}")
    public ResponseEntity<TodoUpdateRecord> updateTodoContent(@PathVariable Long id, @Valid @RequestBody TodoUpdateRecord todoUpdateRecord, @AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>(todoService.updateTodoContent(id, todoUpdateRecord, userDetails),HttpStatus.OK);
    }

    @PatchMapping("/todos/{id}")
    public ResponseEntity<TodoCompleteRecord> updateTodoStatus(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(todoService.updateTodoStatus(id, userDetails),HttpStatus.OK);
    }

    @DeleteMapping("/todos/completed")
    public ResponseEntity<AppUserRecord> clearCompletedTodos(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(todoService.clearCompletedTodos(userDetails),HttpStatus.OK);
    }
}
