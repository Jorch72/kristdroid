package net.teamdentro.kristwallet.exception;

public class InsufficientFundsException extends Exception {
    public InsufficientFundsException() {
    }

    public InsufficientFundsException(String detailMessage) {
        super(detailMessage);
    }

    public InsufficientFundsException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public InsufficientFundsException(Throwable throwable) {
        super(throwable);
    }
}
