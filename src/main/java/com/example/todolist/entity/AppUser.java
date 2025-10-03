package com.example.todolist.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA는 protected 기본 생성자를 권장합니다.
@Getter
@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에 위임하는 IDENTITY 전략을 명시
    @Column(nullable=false, updatable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // final 제거

    @Column(nullable = false)
    @Setter // 비밀번호는 변경될 수 있으므로 Setter를 열어둡니다.
    private String password; // final 제거

    @Column(nullable = false)
    private String role; // final 제거

    // 양방향 연관관계에서 리스트는 NPE 방지를 위해 초기화해주는 것이 안전합니다.
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "appUser", orphanRemoval = true)
    private List<Todo> todos = new ArrayList<>();

    // 생성 시점에 값을 초기화하기 위한 생성자 (Builder 패턴도 좋은 대안입니다)
    public AppUser(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public void addTodo(Todo todo) {
        this.todos.add(todo);
        todo.setAppUser(this);
    }
}
