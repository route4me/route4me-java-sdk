package com.route4me.sdk.exception;

public class APIException extends Exception {

    private static final long serialVersionUID = 1L;

    public APIException(String message) {
        super(message);
    }

    public APIException(String message, Throwable e) {
        super(message, e);
    }
    
    public APIException(Exception e) {
        super(e.getMessage());
    }
}
