package com.ej.authentication.exceptions;

public class NewPasswordDivergentException extends RuntimeException {
    public NewPasswordDivergentException(String msg) {
        super(msg);
    }
}
