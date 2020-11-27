package dev.zodo.openfaas.api;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.BasicAuthentication;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.WebTarget;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

final class ApiClientBuilder<T> {
    private final Class<T> clazz;
    private final WebTarget target;
    private final Map<String, String> headers = new HashMap<>();

    private ApiClientBuilder(Class<T> clazz, URI url) {
        this.clazz = clazz;
        target = ClientBuilder.newClient().target(url);
    }

    public ApiClientBuilder<T> authBasic(String username, String password) {
        target.register(new BasicAuthentication(username, password));
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
        ResteasyWebTarget rtarget = (ResteasyWebTarget) target;

        return rtarget.proxy(clazz);
    }

    public static <T> ApiClientBuilder<T> newBuilder(Class<T> clazz, URI uri) {
        return new ApiClientBuilder<>(clazz, uri);
    }
}
