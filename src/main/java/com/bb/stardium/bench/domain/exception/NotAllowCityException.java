package com.bb.stardium.bench.domain.exception;

public class NotAllowCityException extends RuntimeException {
    public NotAllowCityException() {
        super("현재 지원되지 않는 지역입니다.");
    }
}
