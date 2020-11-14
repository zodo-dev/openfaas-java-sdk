package dev.zodo.openfaas.exceptions;

public class OpenfaasSdkException extends RuntimeException {
    public OpenfaasSdkException(String message) {
        super(message);
    }

    public OpenfaasSdkException(String message, Throwable cause) {
        super(message, cause);
    }

    public OpenfaasSdkException(Throwable cause) {
        super(cause);
    }

    public OpenfaasSdkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
