package com.bb.stardium.player.exception;

public class AuthenticationFailException extends RuntimeException{
    public AuthenticationFailException() {
        this("사용자 인증에 실패했습니다.");
    }

    public AuthenticationFailException(final String message) {
        super(message);
    }

    public AuthenticationFailException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AuthenticationFailException(final Throwable cause) {
        super(cause);
    }
}
