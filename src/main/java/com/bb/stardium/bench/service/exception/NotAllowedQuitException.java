package com.bb.stardium.bench.service.exception;

public class NotAllowedQuitException extends RuntimeException {
    public NotAllowedQuitException() {
        super("풀방 상태인 방에서는 나갈 수 없습니다.");
    }
}
