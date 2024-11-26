package com.ej.authentication.exceptions;

public class UsernameUniqueViolationException extends RuntimeException {

    public UsernameUniqueViolationException(String msg) {
        super(msg);
    }
}
