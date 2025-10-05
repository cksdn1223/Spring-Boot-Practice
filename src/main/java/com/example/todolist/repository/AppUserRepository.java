package com.example.todolist.repository;

import com.example.todolist.entity.AppUser;
import com.example.todolist.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String name);
}
