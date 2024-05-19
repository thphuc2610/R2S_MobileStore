package com.example.mobile_store.exception;

import lombok.experimental.StandardException;

@StandardException
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
