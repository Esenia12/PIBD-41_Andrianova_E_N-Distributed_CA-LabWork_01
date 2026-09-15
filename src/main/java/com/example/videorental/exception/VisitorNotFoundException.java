package com.example.videorental.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VisitorNotFoundException extends RuntimeException {
    public VisitorNotFoundException(Long id) {
        super("Visitor not found with id: " + id);
    }
}