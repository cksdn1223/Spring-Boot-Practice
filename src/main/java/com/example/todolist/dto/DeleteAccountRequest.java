package com.example.todolist.dto;

import jakarta.validation.constraints.NotBlank;

public record DeleteAccountRequest(@NotBlank(message = "비밀번호를 입력해주세요.") String password) {}
