package dev.zodo.openfaas.api;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.client.WebTarget;
import java.net.URI;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
abstract class BaseApi<T> {
    private final URI uri;
    private String username;
    private String password;
    private Supplier<Map<String, String>> customHeaderSupplier;
    private final Function<WebTarget, T> newInstanceBuilder;

    protected BaseApi(URI uri, String username, String password, Function<WebTarget, T> newInstanceBuilder) {
        this.uri = uri;
        this.username = username;
        this.password = password;
        this.newInstanceBuilder = newInstanceBuilder;
    }

    protected BaseApi(URI uri, Supplier<Map<String, String>> customHeaderSupplier, Function<WebTarget, T> newInstanceBuilder) {
        this.uri = uri;
        this.customHeaderSupplier = customHeaderSupplier;
        this.newInstanceBuilder = newInstanceBuilder;
    }

    protected ApiClientBuilder<T> newClient() {
        return newClient(false);
    }

    protected ApiClientBuilder<T> newClient(boolean requireAuth) {
        final ApiClientBuilder<T> clientBuilder = ApiClientBuilder.newBuilder(uri, newInstanceBuilder);
        if (customHeaderSupplier != null) {
            clientBuilder.withCustomHeaderSupplier(customHeaderSupplier);
        }
        if (requireAuth) {
            return clientBuilder.withAuthBasic(username, password);
        }
        return clientBuilder;
    }

}
