package com.myfeed.exception;

import java.util.HashMap;
import java.util.Map;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.myfeed.response.ErrorResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpectedException.class)
    protected ErrorResponse handleExpectedException(final ExpectedException exception) {
        return new ErrorResponse("예외 Enum값");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = (error instanceof org.springframework.validation.FieldError)
                    ? ((org.springframework.validation.FieldError) error).getField()
                    : error.getObjectName();
            errors.put(fieldName, error.getDefaultMessage());
        });
        System.out.println(errors);
        // {registerDto=비밀번호가 일치하지 않습니다., email=이메일 형식이 올바르지 않습니다.}
        return new ErrorResponse("형식문제");
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                Map.of(
//                        "error", "User Not Found",
//                        "message", ex.getMessage(),
//                        "status", HttpStatus.NOT_FOUND.value()
//                )
//        );

        return new ErrorResponse(HttpStatus.NOT_FOUND.toString());
    }

    @ExceptionHandler(CustomException.class)
    public ErrorResponse handleCustomException(CustomException ex) {
        String message = ex.getMessage();
        String errorCode = ex.getErrorCode();

        return new ErrorResponse(errorCode);
    }
}


