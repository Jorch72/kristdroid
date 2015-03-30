package net.teamdentro.kristwallet.exception;

public class BadValueException extends Exception {
    public BadValueException() {
    }

    public BadValueException(String detailMessage) {
        super(detailMessage);
    }

    public BadValueException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public BadValueException(Throwable throwable) {
        super(throwable);
    }
}
