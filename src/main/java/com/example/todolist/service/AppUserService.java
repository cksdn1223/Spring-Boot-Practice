package com.example.todolist.service;

import com.example.todolist.dto.*;
import com.example.todolist.entity.AppUser;
import com.example.todolist.entity.Todo;
import com.example.todolist.exception.AccessDeniedException;
import com.example.todolist.exception.ResourceNotFoundException;
import com.example.todolist.exception.UsernameAlreadyExistsException;
import com.example.todolist.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public List<AppUserRecord> getAllUser() {
        // 모든 유저를 찾고 record로 만들어 list형태로 리턴
        return appUserRepository.findAll().stream()
                .map(appUser -> new AppUserRecord(appUser.getUsername(), appUser.getRole(), appUser.getTodos())).toList();
    }



    public AppUserRecord getUserInfo(UserDetails userDetails) {
        AppUser appUser = appUserRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(()-> new ResourceNotFoundException("사용자를 찾을 수 없습니다: " + userDetails.getUsername()));
        return new AppUserRecord(appUser.getUsername(), appUser.getRole(), appUser.getTodos());
    }

    @Transactional
    public void deleteUser(DeleteAccountRequest request, UserDetails userDetails) {
        AppUser appUser = appUserRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(()-> new ResourceNotFoundException("사용자를 찾을 수 없습니다: " + userDetails.getUsername()));
        if(!passwordEncoder.matches(request.password(), appUser.getPassword())){
            throw new AccessDeniedException("비밀번호가 일치하지 않습니다.");
        }
        appUserRepository.delete(appUser);
    }

    @Transactional
    public AppUserRecord saveUser(AccountCredentialsRecord request) {
        // record로 입력받은 유저네임으로 유저를 찾음
        Optional<AppUser> searchUser = appUserRepository.findByUsername(request.username());
        // 유저가 없어서 비어있다면
        if (searchUser.isEmpty()) {
            AppUser newUser = new AppUser(request.username(), passwordEncoder.encode(request.password()), "USER");
            AppUser savedUser = appUserRepository.save(newUser);
            // 생성된 사용자 정보를 DTO로 변환하여 반환
            return new AppUserRecord(savedUser.getUsername(), savedUser.getRole(), savedUser.getTodos());
        }
        // searchUser가 비어있지 않다면 회원가입 불가능하니 예외처리
        else throw new UsernameAlreadyExistsException("이미 존재하는 사용자입니다: " + request.username());
    }

    @Transactional
    public void changePassword(ChangePasswordRecord changePasswordRecord , UserDetails userDetails) {
        // 로그인한 유저로 DB에서 정보 찾아 저장
        AppUser loginUser = appUserRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다: " + userDetails.getUsername()));
        // 입력한 현재 패스워드랑 저장된 loginUser의 패스워드가 다르면 예외 발생
        if(!passwordEncoder.matches(changePasswordRecord.currentPassword(), loginUser.getPassword())){
            throw new AccessDeniedException("현재 비밀번호가 일치하지 않습니다.");
        }
        // 일치하다면 새로운 패스워드 암호화후 변경
        loginUser.setPassword(passwordEncoder.encode(changePasswordRecord.newPassword()));
    }
}
