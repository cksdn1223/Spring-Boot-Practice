package com.example.todolist.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRecord(@NotBlank(message = "현재 비밀번호는 필수입니다.") String currentPassword, @NotBlank(message = "새 비밀번호는 필수입니다.") String newPassword) {}
