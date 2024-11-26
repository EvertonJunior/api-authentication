package com.ej.authentication.exceptions;

public class TokenValidUniqueViolationException extends RuntimeException {
    public TokenValidUniqueViolationException(String msg) {
        super(msg);
    }
}
