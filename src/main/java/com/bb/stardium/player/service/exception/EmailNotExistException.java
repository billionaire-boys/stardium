package com.bb.stardium.player.service.exception;

public class EmailNotExistException extends RuntimeException {
    public EmailNotExistException() {
        this("존재하지 않는 사용자 이메일입니다.");
    }

    public EmailNotExistException(final String message) {
        super(message);
    }
}
