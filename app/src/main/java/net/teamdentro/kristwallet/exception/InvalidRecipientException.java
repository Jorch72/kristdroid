package net.teamdentro.kristwallet.exception;

public class InvalidRecipientException extends Exception {
    public InvalidRecipientException() {
    }

    public InvalidRecipientException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidRecipientException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public InvalidRecipientException(Throwable throwable) {
        super(throwable);
    }
}
