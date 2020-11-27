package dev.zodo.openfaas.api;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.Map;
import java.util.function.Supplier;

@Slf4j
abstract class BaseApi<T> {
    private final URI uri;
    private final Class<T> tClass;
    private String username;
    private String password;
    private Supplier<Map<String, String>> customHeaderSupplier;

    protected BaseApi(URI uri, String username, String password, Class<T> tClass) {
        this.uri = uri;
        this.username = username;
        this.password = password;
        this.tClass = tClass;
    }

    protected BaseApi(URI uri, Supplier<Map<String, String>> customHeaderSupplier, Class<T> tClass) {
        this.uri = uri;
        this.customHeaderSupplier = customHeaderSupplier;
        this.tClass = tClass;
    }

    protected ApiClientBuilder<T> newClient() {
        return newClient(false);
    }

    protected ApiClientBuilder<T> newClient(boolean requireAuth) {
        final ApiClientBuilder<T> clientBuilder = ApiClientBuilder.newBuilder(tClass, uri);
        if (customHeaderSupplier != null) {
            clientBuilder.withCustomHeaderSupplier(customHeaderSupplier);
        }
        if (requireAuth) {
            return clientBuilder.withAuthBasic(username, password);
        }
        return clientBuilder;
    }

}
