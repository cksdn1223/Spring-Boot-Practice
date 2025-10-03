package com.example.todolist.dto;

import com.example.todolist.entity.AppUser;
import com.example.todolist.entity.Todo;
import com.example.todolist.repository.AppUserRepository;

import java.util.List;

public record AppUserRecord(String username, String role, List<Todo> todos) {}
