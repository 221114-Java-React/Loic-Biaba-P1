package com.revature.resproject.utils.custom_exceptions;

public class InvalidReimbException extends RuntimeException{
    public InvalidReimbException() {
    }

    public InvalidReimbException(String message) {
        super(message);
    }

    public InvalidReimbException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidReimbException(Throwable cause) {
        super(cause);
    }

    public InvalidReimbException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
