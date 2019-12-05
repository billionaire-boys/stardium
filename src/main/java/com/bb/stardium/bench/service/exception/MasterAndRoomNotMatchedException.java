package com.bb.stardium.bench.service.exception;

public class MasterAndRoomNotMatchedException extends RuntimeException {
    public MasterAndRoomNotMatchedException() {
        super("해당 플레이어가 개설한 방이 아닙니다");
    }
}
