package com.example.todolist.controller;

import com.example.todolist.dto.AccountCredentialsRecord;
import com.example.todolist.dto.AppUserRecord;
import com.example.todolist.dto.ChangePasswordRecord;
import com.example.todolist.dto.DeleteAccountRequest;
import com.example.todolist.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public List<AppUserRecord> getAllUsers(@AuthenticationPrincipal UserDetails userDetails) {
        return appUserService.getAllUser(userDetails);
    }

    @GetMapping("users/me")
    public AppUserRecord getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return appUserService.getUserInfo(userDetails);
    }
    @DeleteMapping("users/me")
    public ResponseEntity<Void> deleteUser(@RequestBody DeleteAccountRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        appUserService.deleteUser(request, userDetails);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("users/save")
    public ResponseEntity<AppUserRecord> saveUser(@RequestBody AccountCredentialsRecord request) {
        return new ResponseEntity<>(appUserService.saveUser(request),HttpStatus.CREATED);
    }

    @PatchMapping("users/changePassword")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRecord changePasswordRecord, @AuthenticationPrincipal UserDetails userDetails){
        appUserService.changePassword(changePasswordRecord, userDetails);
        return ResponseEntity.ok().build();
    }
}
