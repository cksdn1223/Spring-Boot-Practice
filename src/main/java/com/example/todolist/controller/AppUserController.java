package com.example.todolist.controller;

import com.example.todolist.dto.AccountCredentialsRecord;
import com.example.todolist.dto.AppUserRecord;
import com.example.todolist.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping("/all")
    public List<AppUserRecord> getAllUsers() {
        return appUserService.getAllUser();
    }

    @PostMapping("/save")
    public ResponseEntity<AccountCredentialsRecord> saveUser(@RequestBody AccountCredentialsRecord request) {
        return appUserService.saveUser(request);
    }
}
