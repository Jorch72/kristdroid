package net.teamdentro.kristwallet.exception;

public class SelfSendException extends Exception {
    public SelfSendException() {
    }

    public SelfSendException(String detailMessage) {
        super(detailMessage);
    }

    public SelfSendException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public SelfSendException(Throwable throwable) {
        super(throwable);
    }
}
