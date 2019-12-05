package com.bb.stardium.player.service.exception;

public class PlayerNotExistException extends RuntimeException {
    public PlayerNotExistException() {
        this("해당 이메일을 가진 사용자가 존재하지 않습니다.");
    }

    public PlayerNotExistException(final String message) {
        super(message);
    }
}
