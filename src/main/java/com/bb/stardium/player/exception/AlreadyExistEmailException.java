package com.bb.stardium.player.exception;

public class AlreadyExistEmailException extends RuntimeException {
    public AlreadyExistEmailException() {
        this("이미 존재하는 사용자 이메일입니다.");
    }

    public AlreadyExistEmailException(final String message) {
        super(message);
    }

    public AlreadyExistEmailException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AlreadyExistEmailException(final Throwable cause) {
        super(cause);
    }
}
