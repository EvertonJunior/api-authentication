package com.ej.authentication.web.exceptions;

import com.ej.authentication.exceptions.*;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardError> accessDeniedException(HttpServletRequest request,
                                                               AccessDeniedException ex){
        StandardError error = new StandardError(request, HttpStatus.FORBIDDEN, ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> methodArgumentNotValidException(HttpServletRequest request,
                                                                         MethodArgumentNotValidException ex,
                                                                         BindingResult result){
        StandardError error = new StandardError(request, HttpStatus.UNPROCESSABLE_ENTITY, "Dados invalidos", result);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(TokenValidUniqueViolationException.class)
    public ResponseEntity<StandardError> tokenValidUniqueViolationException(HttpServletRequest request,
                                                                            TokenValidUniqueViolationException ex
                                                                         ){
        StandardError error = new StandardError(request, HttpStatus.CONFLICT, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(UsernameUniqueViolationException.class)
    public ResponseEntity<StandardError> UsernameUniqueViolationException(HttpServletRequest request,
                                                                          RuntimeException e){
        StandardError standardError= new StandardError(request, HttpStatus.CONFLICT, e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(standardError);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<StandardError> resourceNotFoundException(HttpServletRequest request,
                                                                   ResourceNotFoundException e){
        StandardError standardError= new StandardError(request, HttpStatus.NOT_FOUND, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(standardError);
    }

    @ExceptionHandler({TokenInvalidException.class})
    public ResponseEntity<StandardError> resourceNotFoundException(HttpServletRequest request,
                                                                   JwtException e){
        StandardError standardError= new StandardError(request, HttpStatus.UNAUTHORIZED, e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(standardError);
    }

    @ExceptionHandler({NewPasswordDivergentException.class})
    public ResponseEntity<StandardError> newPasswordDivergentException(HttpServletRequest request,
                                                                       NewPasswordDivergentException e){
        StandardError standardError= new StandardError(request, HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardError);
    }
}
