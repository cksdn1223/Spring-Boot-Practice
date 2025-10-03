package com.example.todolist.service;

import com.example.todolist.dto.*;
import com.example.todolist.entity.AppUser;
import com.example.todolist.entity.Todo;
import com.example.todolist.exception.AccessDeniedException;
import com.example.todolist.exception.NoContentException;
import com.example.todolist.exception.ResourceNotFoundException;
import com.example.todolist.repository.AppUserRepository;
import com.example.todolist.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final AppUserRepository appUserRepository;
    private static final Logger log = LoggerFactory.getLogger(TodoService.class);

    /**
     * 특정 ID를 가진 Todo를 데이터베이스에서 조회합니다.
     *
     * @param id 조회할 Todo의 고유 ID
     * @return 조회된 Todo 엔티티
     * @throws ResourceNotFoundException 해당 ID를 가진 Todo를 찾을 수 없을 경우
     */
    public Todo findTodoById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(id + " 을 가진 Todo를 찾을 수 없습니다."));
    }
    /**
     * 현재 로그인한 사용자와 Todo의 소유자가 다를 경우 {@link AccessDeniedException} 예외 발생
     * @param todo 검증할 Todo 엔티티
     * @param userDetails 현재 로그인한 사용자의 상세 정보
     * @throws AccessDeniedException Todo의 소유자가 현재 사용자와 일치하지 않을 경우
     */
    private void validateOwnership(Todo todo, UserDetails userDetails) {
        if (!todo.getAppUser().getUsername().equals(userDetails.getUsername())) {
            throw new AccessDeniedException("이 리소스에 접근할 권한이 없습니다.");
        }
    }

/**************************
    API 구현 로직들
***************************/
    @Transactional(readOnly = true)
    public TodoRequestRecord findById(Long id) {
        return new TodoRequestRecord(findTodoById(id).getContent());
    }

    public TodoRequestRecord addTodo(TodoRequestRecord todoRequestRecord, UserDetails userDetails) {
        // 로그를 남기기 위한 로그 메시지
        log.info("Adding new todo for user: {}", userDetails.getUsername());
        // 컨트롤러에서 로그인한 유저의 정보를 가져와 AppUser객체로 저장
        // 로그인한 유저기때문에 예외처리 안해도 될거같지만 안전을 위해 예외처리함
        AppUser appUser = appUserRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(userDetails.getUsername() + " 을 찾을수 없음"));
        // 생성 시점에는 관계를 설정하지 않음 ⬇
        Todo newTodo = new Todo(todoRequestRecord.content(), null);
        appUser.addTodo(newTodo); // 편의 메소드를 통해 양방향 관계를 한번에 설정
        // save 메서드가 객체를 리턴해주기때문에 바로 체이닝메서드 가능
        return new TodoRequestRecord(todoRepository.save(newTodo).getContent());
    }

    @Transactional
    public void deleteTodo(Long id, UserDetails userDetails) {
        // 입력받은 id를 가진 Todo를 찾음 없으면 메서드상에서 예외처리함
        Todo todo = findTodoById(id);
        // 삭제작업이기때문에 확인하기위해 로그 남기기
        // 중요 작업은 WARN 레벨로 남기는게 좋다고함
        log.warn("Deleting todo id: {} by user: {}", id, userDetails.getUsername());
        // 현재 로그인한 유저가 이 todo의 소유가 맞는지 확인하는 메서드 다르면 예외처리
        validateOwnership(todo, userDetails);
        // id를 가진 todo 삭제 단순 삭제기때문에 return은 굳이 하지 않았음
        todoRepository.deleteById(id);
    }

    @Transactional
    public TodoUpdateRecord updateTodoContent(Long id, TodoUpdateRecord todoUpdateRecord, UserDetails userDetails) {
        // 입력받은 id를 가진 Todo를 찾음 없으면 메서드상에서 예외처리함
        Todo todo = findTodoById(id);
        // 현재 로그인한 유저가 이 todo의 소유가 맞는지 확인하는 메서드 다르면 예외처리
        validateOwnership(todo, userDetails);
        // 입력받은 record로 todo의 content를 변경
        todo.setContent(todoUpdateRecord.content());
        // record로 바꾼 content 리턴
        return new TodoUpdateRecord(todo.getContent());
    }

    @Transactional
    public TodoCompleteRecord updateTodoStatus(Long id, UserDetails userDetails) {
        // 입력받은 id를 가진 Todo를 찾음 없으면 메서드상에서 예외처리함
        Todo todo = findTodoById(id);
        // 현재 로그인한 유저가 이 todo의 소유가 맞는지 확인하는 메서드 다르면 예외처리
        validateOwnership(todo, userDetails);
        // todo의 완료상태를 반대상태로 변경
        todo.setCompleted(!todo.isCompleted());
        // record로 상태를 바꾼 todo의 content, 완료상태 리턴
        return new TodoCompleteRecord(todo.getContent(), todo.isCompleted());
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")  // 이 메소드는 ADMIN 역할만 호출 가능
    public AppUserRecord clearCompletedTodos(UserDetails userdetails) {
        // 로그인한 유저의 userdetails을 이용해 AppUser객체로 저장
        AppUser appUser = appUserRepository.findByUsername(userdetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userdetails.getUsername()));
        // 저장한 appUser의 todo 리스트를 불러와서 완료상태가 true인것들을 삭제
        appUser.getTodos().removeIf(Todo::isCompleted);
        // record로 이름 역할 todo리스트들 반환
        return new AppUserRecord(appUser.getUsername(), appUser.getRole(), appUser.getTodos());
    }
}



//    /**
//     * 내용이 {@code null}이거나 비어있는 문자열({@code ""}) 또는 공백 문자열({@code " "})일 경우 예외 발생
//     *
//     * @param content 검증할 Todo의 content
//     * @throws NoContentException 내용이 유효하지 않을 경우 (null, 빈 문자열, 공백 문자열)
//     */
//    // 의존성 추가로 더이상 쓰이지 않음.
//    public void checkContent(String content) {
//        if (content == null || content.isBlank()) {
//            throw new NoContentException("내용은 비워둘 수 없습니다.");
//        }
//    }


//        if (!appUser.getRole().equals("ROLE_ADMIN")) { // 이렇게 하지말고 @PreAuthorize("hasRole('ADMIN')") 이거로 간편하게
//            throw new AccessDeniedException("이 리소스에 접근할 권한이 없습니다.");
//        }