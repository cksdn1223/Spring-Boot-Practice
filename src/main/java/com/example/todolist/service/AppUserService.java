package com.example.todolist.service;

import com.example.todolist.dto.AccountCredentialsRecord;
import com.example.todolist.dto.AppUserRecord;
import com.example.todolist.entity.AppUser;
import com.example.todolist.exception.UsernameAlreadyExistsException;
import com.example.todolist.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public ResponseEntity<AccountCredentialsRecord> saveUser(AccountCredentialsRecord request) {
        // record로 입력받은 유저네임으로 유저를 찾음
        Optional<AppUser> searchUser = appUserRepository.findByUsername(request.username());
        // 유저가 없어서 비어있다면
        if (searchUser.isEmpty()) {
            // 회원가입 가능하니 유저를 저장 비밀번호 암호화 필수
            appUserRepository.save(new AppUser(request.username(), passwordEncoder.encode(request.password()), "USER"));
            // 만들었으니 record와 HttpStatus를 CREATED로 리턴
            return new ResponseEntity<>(request, HttpStatus.CREATED);
        }
        // searchUser가 비어있지 않다면 회원가입 불가능하니 예외처리
        else throw new UsernameAlreadyExistsException("이미 존재하는 사용자입니다: " + request.username());
    }
}
