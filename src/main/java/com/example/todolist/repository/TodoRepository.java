package com.example.todolist.repository;

import com.example.todolist.dto.TodoCompleteRecord;
import com.example.todolist.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findAllByAppUser_Username(String username);
}
