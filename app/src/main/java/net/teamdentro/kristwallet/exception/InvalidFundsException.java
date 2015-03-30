package net.teamdentro.kristwallet.exception;

public class InvalidFundsException extends Exception {
    public InvalidFundsException() {
    }

    public InvalidFundsException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidFundsException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public InvalidFundsException(Throwable throwable) {
        super(throwable);
    }
}
