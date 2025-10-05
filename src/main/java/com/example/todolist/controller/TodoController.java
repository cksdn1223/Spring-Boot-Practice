package com.example.todolist.controller;

import com.example.todolist.dto.AppUserRecord;
import com.example.todolist.dto.TodoCompleteRecord;
import com.example.todolist.dto.TodoRequestRecord;
import com.example.todolist.dto.TodoUpdateRecord;
import com.example.todolist.service.AppUserService;
import com.example.todolist.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Todo Management", description = "투두리스트 관리 API")
public class TodoController {
    private final TodoService todoService;

    @GetMapping("/todos/{id}")
    @Operation(summary = "Id로 투두리스트 찾기", description = "ID로 해당하는 Todo를 찾습니다.")
    public ResponseEntity<TodoRequestRecord> find(@PathVariable Long id) {
        return new ResponseEntity<>(todoService.findById(id),HttpStatus.OK);
    }
    
    @PostMapping("/todos/save")
    @Operation(summary = "투두리스트 추가", description = "입력받은 내용으로 로그인한 유저의 투두리스트를 추가합니다.")
    public ResponseEntity<TodoRequestRecord> addTodo(@Valid @RequestBody TodoRequestRecord todoRequestRecord, @AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>(todoService.addTodo(todoRequestRecord, userDetails),HttpStatus.OK);
    }

    @DeleteMapping("/todos/{id}")
    @Operation(summary = "투두리스트 삭제", description = "ID에 해당하는 로그인한 유저의 투두를 제거합니다.")
    public ResponseEntity<TodoUpdateRecord> deleteTodo(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        todoService.deleteTodo(id, userDetails);
        return ResponseEntity.noContent().build();  // 204 No Content
    }

    @PatchMapping("/todos/content/{id}")
    @Operation(summary = "투두리스트 내용 업데이트", description = "ID에 해당하는 로그인한 유저의 투두의 내용을 업데이트합니다.")
    public ResponseEntity<TodoUpdateRecord> updateTodoContent(@PathVariable Long id, @Valid @RequestBody TodoUpdateRecord todoUpdateRecord, @AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>(todoService.updateTodoContent(id, todoUpdateRecord, userDetails),HttpStatus.OK);
    }

    @PatchMapping("/todos/{id}")
    @Operation(summary = "투두리스트 상태 변경", description = "ID에 해당하는 로그인한 유저의 투두리스트 상태를 변경합니다.")
    public ResponseEntity<TodoCompleteRecord> updateTodoStatus(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(todoService.updateTodoStatus(id, userDetails),HttpStatus.OK);
    }

    @DeleteMapping("/todos/completed")
    @Operation(summary = "완료된 투두 삭제", description = "완료상태인 로그인한 유저의 투두를 제거합니다.")
    public ResponseEntity<AppUserRecord> clearCompletedTodos(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(todoService.clearCompletedTodos(userDetails),HttpStatus.OK);
    }
}
