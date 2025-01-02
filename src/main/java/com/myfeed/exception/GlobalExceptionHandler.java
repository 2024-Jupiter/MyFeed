package com.myfeed.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.myfeed.response.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpectedException.class)
    protected ErrorResponse handleExpectedException(final ExpectedException exception) {
        return new ErrorResponse("예외 Enum값");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = (error instanceof org.springframework.validation.FieldError)
                    ? ((org.springframework.validation.FieldError) error).getField()
                    : error.getObjectName();
            errors.put(fieldName, error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of(
                        "error", "User Not Found",
                        "message", ex.getMessage(),
                        "status", HttpStatus.NOT_FOUND.value()
                )
        );
    }

    // Entity를 찾을 수 없을 때 예외 처리
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFound(EntityNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "존재 하지 않는 엔티티 입니다.");
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // 사용자 삭제 상태 예외 처리
    @ExceptionHandler(UserDeletedException.class)
    public ResponseEntity<Map<String, String>> handleUserDeleted(UserDeletedException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "삭제된 사용자 입니다.");
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 게시글이 차단된 상태 예외 처리
    @ExceptionHandler(PostBlockedException.class)
    public ResponseEntity<Map<String, String>> handlePostBlocked(PostBlockedException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "차단된 게시글 입니다.");
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // 게시글이 차단 해제된 상태 예외 처리
    @ExceptionHandler(PostUnBlockedException.class)
    public ResponseEntity<Map<String, String>> handlePostUnBlocked(PostUnBlockedException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "차단 해제된 게시글 입니다.");
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // 댓글이 차단된 상태 예외 처리
    @ExceptionHandler(ReplyBlockedException.class)
    public ResponseEntity<Map<String, String>> handleReplyBlocked(ReplyBlockedException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "차단된 댓글 입니다.");
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // 댓글이 차단 해제된 상태 예외 처리
    @ExceptionHandler(ReplyUnBlockedException.class)
    public ResponseEntity<Map<String, String>> handleReplyUnBlocked(ReplyUnBlockedException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "차단 해제된 댓글 입니다.");
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // 신고 처리 대기 상태 예외 처리
    @ExceptionHandler(ReportPendingException.class)
    public ResponseEntity<Map<String, String>> handleReportPending(ReportPendingException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "신고 처리 대기 상태 중 입니다.");
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // 신고 처리 완료 상태 예외 처리
    @ExceptionHandler(ReportCompletedException.class)
    public ResponseEntity<Map<String, String>> handleReportCompleted(ReportCompletedException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "신고 처리 완료 상태 입니다.");
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // 잘못된 카테고리 선택 예외 처리
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "잘못된 카테고리를 선택 했습니다.");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // 잘못된 이미지 형식 예외 처리
    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<Map<String, String>> handleImageUploadException(ImageUploadException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "이미지 업로드에 실패 했습니다..");
        response.put("message", ex.getMessage());  // 이미지 업로드 실패 메시지 반환

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // 400 Bad Request 상태 코드 반환
    }
}


