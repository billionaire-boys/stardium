package com.bb.stardium.bench.service.exception;

public class NotFoundRoomException extends RuntimeException {
    public NotFoundRoomException() {
        super("Room not found exception occurred!");
    }
}
