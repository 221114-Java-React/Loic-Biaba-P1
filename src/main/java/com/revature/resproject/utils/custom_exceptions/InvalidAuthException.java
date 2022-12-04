package com.revature.resproject.utils.custom_exceptions;

public class InvalidAuthException extends RuntimeException {

    private String message;
    public InvalidAuthException() {
        super();
    }

    public InvalidAuthException(String message) {
        super(message);
    }

    public InvalidAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAuthException(Throwable cause) {
        super(cause);
    }

    public InvalidAuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
