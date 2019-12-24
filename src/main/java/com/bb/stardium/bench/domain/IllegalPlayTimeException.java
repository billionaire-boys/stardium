package com.bb.stardium.bench.domain;

public class IllegalPlayTimeException extends RuntimeException {
    public IllegalPlayTimeException() {
    }

    public IllegalPlayTimeException(String message) {
        super(message);
    }
}
