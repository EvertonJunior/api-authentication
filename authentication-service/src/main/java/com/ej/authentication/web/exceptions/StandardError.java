package com.ej.authentication.web.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class StandardError {

    private String path;
    private String method;
    private int status;
    private String statusText;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> errors;

    public StandardError(HttpServletRequest request, HttpStatus status, String message){
        this.path = request.getRequestURI();
        this.method = request.getMethod();
        this.status = status.value();
        this.statusText = status.getReasonPhrase();
        this.message = message;
    }

    public StandardError(HttpServletRequest request, HttpStatus status, String message, BindingResult bindingResult){
        this.path = request.getRequestURI();
        this.method = request.getMethod();
        this.status = status.value();
        this.statusText = status.getReasonPhrase();
        this.message = message;
        addErrors(bindingResult);
    }

    private void addErrors(BindingResult bindingResult) {
        this.errors = new HashMap<>();
        for(FieldError error: bindingResult.getFieldErrors()){
            this.errors.put(error.getField(), error.getDefaultMessage());
        }
    }


}
