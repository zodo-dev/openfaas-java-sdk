package dev.zodo.openfaas.api.exceptions;

import dev.zodo.openfaas.exceptions.OpenfaasSdkException;

public class OFUnexpectedException extends OpenfaasSdkException {
    public OFUnexpectedException() {
        super("Unexpected Error");
    }

    public OFUnexpectedException(Throwable ex) {
        super("Unexpected Error", ex);
    }
}
