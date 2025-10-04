package com.example.todolist.dto;

import com.example.todolist.entity.Todo;
import jakarta.validation.constraints.NotBlank;

public record TodoRequestRecord(@NotBlank(message = "내용은 비워둘 수 없습니다.") String content) {}
