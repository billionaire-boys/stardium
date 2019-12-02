package com.bb.stardium.player.exception;

public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException() {
    }

    public EmailAlreadyExistException(final String message) {
        super(message);
    }

    public EmailAlreadyExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EmailAlreadyExistException(final Throwable cause) {
        super(cause);
    }
}
