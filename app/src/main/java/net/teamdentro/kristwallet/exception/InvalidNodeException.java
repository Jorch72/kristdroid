package net.teamdentro.kristwallet.exception;

public class InvalidNodeException extends Exception {
    public InvalidNodeException() {
    }

    public InvalidNodeException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidNodeException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public InvalidNodeException(Throwable throwable) {
        super(throwable);
    }
}
