package com.project.realtime_collaborative_doc_editing.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFound  extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final HttpStatus httpStatus;

    public UserNotFound(HttpStatus httpStatus) {
        super();
        this.httpStatus = httpStatus;
    }

    public UserNotFound(String msg, HttpStatus httpStatus) {
        super(msg);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
