package com.example.todolist.exception;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice   // @RestController에서 발생하는 예외를 전역적으로 처리합니다.
public class GlobalExceptionHandler {

    /**
     * 리소스를 찾지 못했을 때 발생하는 사용자 지정 예외
     * @param ex - ResourceNotFoundException
     * @return 404 HttpStatus.NOT_FOUND
     */
    // @ExceptionHandler: 특정 예외 클래스를 지정하여 처리할 메소드를 정의합니다.
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        // 클라이언트에게 보여줄 에러 메시지를 일관된 형식으로 구성합니다.
        Map<String, String> errorResponse = Map.of(
                "status", "404",
                "error", "Not Found",
                "message", ex.getMessage() // 서비스 계층에서 전달한 메시지를 포함합니다.
        );
        // 구성된 에러 메시지와 함께 HTTP 404 (Not Found) 상태 코드를 응답합니다.
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * 권한이 없을 때 발생하는 예외 처리
     * @param ex - AccessDeniedException
     * @return 403 HttpStatus.FORBIDDEN
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, String> errorResponse = Map.of(
                "status", "403",
                "error", "Forbidden",
                "message", ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Todo의 Content를 조작할때 비어있거나 공백이면 발생하는 예외 처리
     * @param ex - NoContentException
     * @return 400 HttpStatus.BAD_REQUEST
     */
    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<Map<String, String>> handleNoContentException(NoContentException ex) {
        Map<String, String> errorResponse = Map.of(
                "status", "400",
                "error", "Bad Request",
                "message", ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * 가입하려는 유저의 이름이 이미 DB에 있을때 발생하는 예외 처리
     * @param ex UsernameAlreadyExistsException
     * @return 409 HttpStatus.CONFLICT
     */
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        Map<String, String> errorResponse = Map.of(
                "status", "409",
                "error", "Conflict",
                "message", ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * 어노테이션을 통한 입력값 검증에 실패했을 때, 어떤 필드에서 어떤 오류가 발생했는지 상세하게 알려주는 JSON 응답을 자동으로 생성하는 역할
     * @param ex
     * @return {@code 400-HttpStatus.BAD_REQUEST}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> errorResponse = Map.of(
                "status", "400",
                "error", "Bad Request",
                "details", errors
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
