package com.example.mobile_store.exception;


public class NotFoundException extends RuntimeException {
    public NotFoundException(long id) {
        super("Not found with id: " + id);
    }
}
