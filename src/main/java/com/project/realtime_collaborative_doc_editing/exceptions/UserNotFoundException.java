package com.project.realtime_collaborative_doc_editing.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final HttpStatus httpStatus;

    public UserNotFoundException(HttpStatus httpStatus) {
        super();
        this.httpStatus = httpStatus;
    }

    public UserNotFoundException(String msg, HttpStatus httpStatus) {
        super(msg);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}