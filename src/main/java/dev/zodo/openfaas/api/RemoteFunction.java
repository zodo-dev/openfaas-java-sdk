package dev.zodo.openfaas.api;

import dev.zodo.openfaas.api.async.AsyncRequest;
import dev.zodo.openfaas.api.async.AsyncResponse;
import dev.zodo.openfaas.api.sync.SyncRequest;
import dev.zodo.openfaas.api.sync.SyncResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface RemoteFunction<T, R> {
    String getName();

    String getUri();

    String getCallbackEndpoint();

    Class<R> getReturnType();

    default Map<String, String> getHeaders() {
        return new HashMap<>();
    }

    default SyncRequest<T> buildRequest(T objData) {
        SyncRequest<T> req = new SyncRequest<>(getName(), objData);
        Optional.ofNullable(getHeaders())
                .ifPresent(headers -> headers.forEach(req::addHeader));
        return req;
    }

    default AsyncRequest<T> buildAsyncRequest(T objData) {
        AsyncRequest<T> req = new AsyncRequest<>(getName(), getCallbackEndpoint(), objData);
        Optional.ofNullable(getHeaders())
                .ifPresent(headers -> headers.forEach(req::addHeader));
        return req;
    }

    default OpenfaasApi getOpenfaasApi() {
        return OpenfaasApi.getInstance(getUri());
    }

    default SyncResponse<R> call(T objData) {
        SyncRequest<T> req = buildRequest(objData);
        return getOpenfaasApi().callFunction(req, getReturnType());
    }

    default CompletableFuture<SyncResponse<R>> callFuture(T objData) {
        SyncRequest<T> req = buildRequest(objData);
        return getOpenfaasApi().callFunctionFuture(req, getReturnType());
    }

    default AsyncResponse asyncCall(T objData) {
        return getOpenfaasApi().callAsyncFunction(buildAsyncRequest(objData));
    }

}
