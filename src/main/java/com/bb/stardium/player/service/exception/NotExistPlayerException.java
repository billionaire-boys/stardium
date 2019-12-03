package com.bb.stardium.player.service.exception;

public class NotExistPlayerException extends RuntimeException {
    public NotExistPlayerException() {
        this("존재하지 않는 사용자 이메일입니다.");
    }

    public NotExistPlayerException(final String message) {
        super(message);
    }

    public NotExistPlayerException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NotExistPlayerException(final Throwable cause) {
        super(cause);
    }
}
