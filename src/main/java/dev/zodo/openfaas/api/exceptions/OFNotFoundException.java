package dev.zodo.openfaas.api.exceptions;

import dev.zodo.openfaas.exceptions.OpenfaasSdkException;

public class OFNotFoundException extends OpenfaasSdkException {
    public OFNotFoundException(String msg) {
        super(msg);
    }
}
