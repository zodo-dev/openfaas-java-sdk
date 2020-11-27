package dev.zodo.openfaas.exceptions;

import dev.zodo.openfaas.i18n.Bundles;

public class OpenfaasSdkUnexpectedException extends OpenfaasSdkException {
    public OpenfaasSdkUnexpectedException() {
        super(Bundles.getString("error.unexpected.error"));
    }

    public OpenfaasSdkUnexpectedException(Throwable ex) {
        super(Bundles.getString("error.unexpected.error"), ex);
    }
}
