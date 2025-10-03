package com.example.todolist;

import com.example.todolist.entity.AppUser;
import com.example.todolist.entity.Todo;
import com.example.todolist.repository.AppUserRepository;
import com.example.todolist.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class TodolistApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(TodolistApplication.class, args);
	}

	private final AppUserRepository appUserRepository;
	private final TodoRepository todoRepository;
	private final PasswordEncoder passwordEncoder;
	@Override
	public void run(String... args) throws Exception {
		AppUser appUser = new AppUser("test", passwordEncoder.encode("test"), "ADMIN");
		AppUser appUser2 = new AppUser("test2", passwordEncoder.encode("test2"), "ADMIN");
		appUserRepository.save(appUser);
		appUserRepository.save(appUser2);
		todoRepository.save(new Todo("Test TodoList1",appUser));
		todoRepository.save(new Todo("Test TodoList2",appUser));
		todoRepository.save(new Todo("Test TodoList3",appUser));
		todoRepository.save(new Todo("Test TodoList4",appUser2));
	}
}
