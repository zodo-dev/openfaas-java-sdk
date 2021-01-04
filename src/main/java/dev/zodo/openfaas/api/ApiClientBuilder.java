package dev.zodo.openfaas.api;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.WebTarget;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

final class ApiClientBuilder<T> {
    private final WebTarget target;
    private final Map<String, String> headers = new HashMap<>();
    private Supplier<Map<String, String>> customHeaderSupplier;
    private Function<WebTarget, T> newInstanceBuilder;

    private ApiClientBuilder(URI url, Function<WebTarget, T> newInstanceBuilder) {
        target = ClientBuilder.newClient().target(url);
        this.newInstanceBuilder = newInstanceBuilder;
    }

    public ApiClientBuilder<T> withAuthBasic(String username, String password) {
        String passwdEncoded = Base64.getEncoder()
                .encodeToString(String.format("%s:%s", username, password).getBytes(StandardCharsets.UTF_8));
        addHeader("Authorization", "Basic " + passwdEncoded);
        return this;
    }

    public ApiClientBuilder<T> withCustomHeaderSupplier(Supplier<Map<String, String>> oauthHeaderSupplier) {
        this.customHeaderSupplier = oauthHeaderSupplier;
        return this;
    }

    public ApiClientBuilder<T> addHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public ApiClientBuilder<T> addHeaders(Map<String, String> headersToAdd) {
        if (headersToAdd == null || headersToAdd.isEmpty()) {
            return this;
        }
        headers.putAll(headersToAdd);
        return this;
    }

    public T build() {
        if (!headers.isEmpty()) {
            ClientRequestFilter requestFilter = clientReqContext -> this.headers.forEach(clientReqContext.getHeaders()::add);
            target.register(requestFilter);
        }
        Map<String, String> oauthHeaders = Optional.ofNullable(customHeaderSupplier)
                .map(Supplier::get)
                .orElse(null);
        if (oauthHeaders != null && !oauthHeaders.isEmpty()) {
            ClientRequestFilter requestFilter = clientReqContext -> oauthHeaders.forEach(clientReqContext.getHeaders()::add);
            target.register(requestFilter);
        }
        return newInstanceBuilder.apply(target);
    }

    public static <T> ApiClientBuilder<T> newBuilder(URI uri, Function<WebTarget, T> newInstanceBuilder) {
        return new ApiClientBuilder<>(uri, newInstanceBuilder);
    }
}
