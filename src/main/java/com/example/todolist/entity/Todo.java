package com.example.todolist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable=false, updatable = false)
    private Long id;

    @Column(nullable = false)
    @Setter
    private String content;

    @Column(nullable = false)
    @Setter
    private boolean isCompleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appUser")
    @JsonIgnore
    @Setter
    private AppUser appUser;

    public Todo(String content, AppUser appUser) {
        this.content = content;
        this.isCompleted = false;
        this.appUser = appUser;
    }
}
