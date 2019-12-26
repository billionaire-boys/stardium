package com.bb.stardium.bench.service.exception;

public class AlreadyJoinedException extends RuntimeException {
    public AlreadyJoinedException() {
        super("이미 참여하고 있는 방입니다.");
    }
}
