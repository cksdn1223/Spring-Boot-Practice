package com.example.todolist.controller;

import com.example.todolist.dto.AccountCredentialsRecord;
import com.example.todolist.dto.AppUserRecord;
import com.example.todolist.dto.ChangePasswordRecord;
import com.example.todolist.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping("users/all")
    @PreAuthorize("hasRole('ADMIN')") // ADMIN 역할을 가진 사용자만 접근 가능
    public List<AppUserRecord> getAllUsers() {
        return appUserService.getAllUser();
    }

    @GetMapping("users/me")
    public AppUserRecord getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return appUserService.getUserInfo(userDetails);
    }
    @PostMapping("users/save")
    public ResponseEntity<AccountCredentialsRecord> saveUser(@RequestBody AccountCredentialsRecord request) {
        return appUserService.saveUser(request);
    }

    @PatchMapping("users/changePassword")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRecord changePasswordRecord, @AuthenticationPrincipal UserDetails userDetails){
        appUserService.changePassword(changePasswordRecord, userDetails);
        return ResponseEntity.ok().build();
    }
}
