package dev.zodo.openfaas.api;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;

@Slf4j
abstract class BaseApi<T> {
    private final URI uri;
    private final String username;
    private final String password;
    private final Class<T> tClass;

    protected BaseApi(URI uri, String username, String password, Class<T> tClass) {
        this.uri = uri;
        this.username = username;
        this.password = password;
        this.tClass = tClass;
    }

    protected ApiClientBuilder<T> newClient() {
        return newClient(false);
    }

    protected ApiClientBuilder<T> newClient(boolean requireAuth) {
        final ApiClientBuilder<T> clientBuilder = ApiClientBuilder.newBuilder(tClass, uri);
        if (requireAuth) {
            return clientBuilder.authBasic(username, password);
        }
        return clientBuilder;
    }

}
