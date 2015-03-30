package net.teamdentro.kristwallet.exception;

public class UnknownException extends Exception {
    public UnknownException() {
    }

    public UnknownException(String detailMessage) {
        super(detailMessage);
    }

    public UnknownException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UnknownException(Throwable throwable) {
        super(throwable);
    }
}
