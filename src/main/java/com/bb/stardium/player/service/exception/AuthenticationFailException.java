package com.bb.stardium.player.service.exception;

public class AuthenticationFailException extends RuntimeException {
    public AuthenticationFailException() {
        super("사용자 인증에 실패했습니다.");
    }
}
