package com.idf.controller.exception;

public class RestControllerException extends RuntimeException {

    public RestControllerException() {
        super();
    }

    public RestControllerException(String message) {
        super(message);
    }

    public RestControllerException(String message, Throwable cause) {
        super(message, cause);
    }

}
