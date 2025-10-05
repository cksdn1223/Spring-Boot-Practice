package com.example.todolist.controller;

import com.example.todolist.dto.*;
import com.example.todolist.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "User Management", description = "사용자 관리 API")
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping("users/all")
    @Operation(summary = "모든 사용자 목록 조회", description = "시스템에 등록된 모든 사용자의 목록을 조회합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AppUserRecord> getAllUsers() {
        return appUserService.getAllUser();
    }



    @GetMapping("users/me")
    @Operation(summary = "내 정보 조회", description = "현재 로그인된 사용자의 정보를 조회합니다.")
    public AppUserRecord getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return appUserService.getUserInfo(userDetails);
    }
    @DeleteMapping("users/me")
    @Operation(summary = "회원 탈퇴", description = "현재 로그인된 사용자의 계정을 삭제합니다. 비밀번호 확인이 필요합니다.")
    public ResponseEntity<Void> deleteUser(@Valid @RequestBody DeleteAccountRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        appUserService.deleteUser(request, userDetails);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("users/save")
    @Operation(summary = "회원 가입", description = "새로운 사용자 계정을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "회원 가입 성공")
    @ApiResponse(responseCode = "409", description = "이미 존재하는 사용자 이름")
    public ResponseEntity<AppUserRecord> saveUser(@Valid @RequestBody AccountCredentialsRecord request) {
        return new ResponseEntity<>(appUserService.saveUser(request),HttpStatus.CREATED);
    }

    @PatchMapping("users/changePassword")
    @Operation(summary = "비밀번호 변경", description = "현재 로그인된 사용자의 비밀번호를 변경합니다.")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRecord changePasswordRecord, @AuthenticationPrincipal UserDetails userDetails){
        appUserService.changePassword(changePasswordRecord, userDetails);
        return ResponseEntity.ok().build();
    }
}
