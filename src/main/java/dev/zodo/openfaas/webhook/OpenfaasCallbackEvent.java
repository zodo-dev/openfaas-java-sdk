package dev.zodo.openfaas.webhook;

import dev.zodo.openfaas.api.async.AsyncCallbackResponse;

public interface OpenfaasCallbackEvent<T> {
    default void consume(AsyncCallbackResponse<T> asyncResponse) {
    }
}
