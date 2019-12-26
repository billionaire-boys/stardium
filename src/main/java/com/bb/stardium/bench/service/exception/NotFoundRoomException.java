package com.bb.stardium.bench.service.exception;

public class NotFoundRoomException extends RuntimeException {
    public NotFoundRoomException() {
        super("존재하지 않는 방입니다.");
    }
}
