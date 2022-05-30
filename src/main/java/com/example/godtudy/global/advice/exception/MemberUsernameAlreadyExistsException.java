package com.example.godtudy.global.advice.exception;

public class MemberUsernameAlreadyExistsException extends RuntimeException {
    public MemberUsernameAlreadyExistsException() {}

    public MemberUsernameAlreadyExistsException(String message) {
        super(message);
    }
    public MemberUsernameAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
