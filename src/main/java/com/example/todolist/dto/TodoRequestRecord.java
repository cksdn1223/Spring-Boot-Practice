package com.example.todolist.dto;

import com.example.todolist.entity.Todo;

public record TodoRequestRecord(@NotBlank(message = "내용은 비워둘 수 없습니다.") String content) {}
