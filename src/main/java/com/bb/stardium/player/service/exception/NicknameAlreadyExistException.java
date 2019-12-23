package com.bb.stardium.player.service.exception;

public class NicknameAlreadyExistException extends RuntimeException {
    public NicknameAlreadyExistException() {
        super("이미 존재하는 사용자의 닉네임입니다.");
    }
}
