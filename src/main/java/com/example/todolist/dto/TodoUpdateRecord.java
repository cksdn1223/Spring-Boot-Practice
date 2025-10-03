package com.example.todolist.dto;

public record TodoUpdateRecord(@NotBlank(message = "내용은 비워둘 수 없습니다.") String content) {
}
