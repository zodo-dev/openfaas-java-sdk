package dev.zodo.openfaas.api;

import dev.zodo.openfaas.api.async.AsyncRequest;
import dev.zodo.openfaas.api.async.AsyncResponse;
import dev.zodo.openfaas.api.sync.SyncRequest;
import dev.zodo.openfaas.api.sync.SyncResponse;
import dev.zodo.openfaas.exceptions.OpenfaasSdkNotFoundException;
import dev.zodo.openfaas.exceptions.OpenfaasSdkUnexpectedException;
import dev.zodo.openfaas.i18n.Bundles;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static dev.zodo.openfaas.util.Constants.NOT_FOUND_MSG;

@Slf4j
public final class OpenfaasApi extends BaseApi<ApiInterface> {

    private OpenfaasApi(URI uri, Supplier<Map<String, String>> customHeaderSupplier, ResteasyClient client) {
        super(uri, customHeaderSupplier, ApiInterface.class, client);
    }

    public static OpenfaasApi getInstance(String uri, ResteasyClient client) {
        return getInstance(URI.create(uri), null, client);
    }

    public static OpenfaasApi getInstance(URI uri, ResteasyClient client) {
        return getInstance(uri, null, client);
    }

    public static OpenfaasApi getInstance(String uri, Supplier<Map<String, String>> customHeaderSupplier, ResteasyClient client) {
        return getInstance(URI.create(uri), customHeaderSupplier, client);
    }

    public static OpenfaasApi getInstance(URI uri, Supplier<Map<String, String>> customHeaderSupplier, ResteasyClient client) {
        return new OpenfaasApi(uri, customHeaderSupplier, client);
    }

    public boolean healthz() {
        final Response response = newClient().build().healthz();
        return response.getStatus() == Status.OK.getStatusCode();
    }

    public <R, T> SyncResponse<R> callFunction(SyncRequest<T> syncRequest, Class<R> returnType) {
        Response response = newClient()
                .addHeaders(syncRequest.getHeaders())
                .build()
                .callFunction(syncRequest.getFunctionName(), syncRequest.getBody());
        if (response.getStatus() == Status.OK.getStatusCode()) {
            return SyncResponse.fromResponse(response, returnType);
        }
        if (response.getStatus() == Status.NOT_FOUND.getStatusCode()) {
            throw new OpenfaasSdkNotFoundException(Bundles.getString(NOT_FOUND_MSG, syncRequest.getFunctionName()));
        }
        throw new OpenfaasSdkUnexpectedException();
    }

    public <R, T> CompletableFuture<SyncResponse<R>> callFunctionFuture(SyncRequest<T> syncRequest, Class<R> returnType) {
        CompletableFuture<SyncResponse<R>> future = new CompletableFuture<>();
        try {
            final SyncResponse<R> result = callFunction(syncRequest, returnType);
            future.complete(result);
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    public <T> AsyncResponse callAsyncFunction(AsyncRequest<T> asyncRequest) {
        Response response = newClient()
                .addHeaders(asyncRequest.getHeaders())
                .build()
                .callAsyncFunction(asyncRequest.getFunctionName(), asyncRequest.getBody());
        if (response.getStatus() == Status.ACCEPTED.getStatusCode() || response.getStatus() == Status.OK.getStatusCode()) {
            return AsyncResponse.fromResponse(response);
        }
        if (response.getStatus() == Status.NOT_FOUND.getStatusCode()) {
            throw new OpenfaasSdkNotFoundException(Bundles.getString(NOT_FOUND_MSG, asyncRequest.getFunctionName()));
        }
        throw new OpenfaasSdkUnexpectedException();
    }

}
