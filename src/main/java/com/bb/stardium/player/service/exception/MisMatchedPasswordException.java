package com.bb.stardium.player.service.exception;

public class MisMatchedPasswordException extends RuntimeException {
    public MisMatchedPasswordException() {
        super("비밀번호와 확인비밀번호가 맞지 않습니다.");
    }
}
