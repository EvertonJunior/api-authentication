package com.ej.authentication.exceptions;

public class TokenInvalidException extends RuntimeException {
    public TokenInvalidException(String msg) {
        super(msg);
    }
}
