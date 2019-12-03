package com.bb.stardium.player.service.exception;

public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException() {
        this("이미 존재하는 사용자 이메일입니다.");
    }

    public EmailAlreadyExistException(final String message) {
        super(message);
    }
}
