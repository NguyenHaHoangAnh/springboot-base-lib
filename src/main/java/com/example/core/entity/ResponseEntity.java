package com.example.core.entity;

import org.springframework.http.HttpStatus;

public class ResponseEntity<T> {
    private HttpStatus httpStatus;

    private T body;

    public ResponseEntity(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public ResponseEntity(T body, HttpStatus httpStatus) {
        this.body = body;
        this.httpStatus = httpStatus;
    }
}
