package dev.zodo.openfaas.api;

import dev.zodo.openfaas.api.exceptions.OFNotFoundException;
import dev.zodo.openfaas.api.exceptions.OFUnexpectedException;
import dev.zodo.openfaas.api.model.FunctionInfo;
import dev.zodo.openfaas.api.model.Info;
import dev.zodo.openfaas.config.Bundles;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static dev.zodo.openfaas.config.OpenfaasSdkProperties.OPENFAAS_PROPS;
import static dev.zodo.openfaas.util.Constants.NOT_FOUND_MSG;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OpenfaasApi {

    public static OpenfaasApi getInstance() {
        return new OpenfaasApi();
    }

    private ApiClientBuilder<ApiInterface> newClient() {
        return newClient(false);
    }

    private ApiClientBuilder<ApiInterface> newClient(boolean requireAuth) {
        final ApiClientBuilder<ApiInterface> clientBuilder = ApiClientBuilder.newBuilder(ApiInterface.class, OPENFAAS_PROPS.url());
        if (requireAuth) {
            return clientBuilder.authBasic(OPENFAAS_PROPS.username(), OPENFAAS_PROPS.password());
        }
        return clientBuilder;
    }

    public boolean healthz() {
        final Status status = newClient().build().healthz();
        return status == Status.OK;
    }

    public Info systemInfo() {
        Response response = newClient(true).build().systemInfo();
        if (response.getStatus() == Status.OK.getStatusCode()) {
            return response.readEntity(Info.class);
        }
        if (response.getStatus() == Status.NOT_FOUND.getStatusCode()) {
            throw new OFNotFoundException(Bundles.getString("provider.not.support.endpoint"));
        }
        throw new OFUnexpectedException();
    }

    public List<FunctionInfo> listFunctions() {
        return newClient(true).build().listFunctions();
    }

    public FunctionInfo infoFunction(String functionName) {
        Response response = newClient(true).build().infoFunction(functionName);
        if (response.getStatus() == Status.OK.getStatusCode()) {
            return response.readEntity(FunctionInfo.class);
        }
        if (response.getStatus() == Status.NOT_FOUND.getStatusCode()) {
            throw new OFNotFoundException(Bundles.getString(NOT_FOUND_MSG, functionName));
        }
        throw new OFUnexpectedException();
    }

    public <R, T> SyncResponse<R> callFunction(SyncRequest<T> syncRequest, Class<R> returnType) {
        Response response = newClient().build().callFunction(syncRequest.getFunctionName(), syncRequest.getBody());
        if (response.getStatus() == Status.OK.getStatusCode()) {
            return SyncResponse.fromResponse(response, returnType);
        }
        if (response.getStatus() == Status.NOT_FOUND.getStatusCode()) {
            throw new OFNotFoundException(Bundles.getString(NOT_FOUND_MSG, syncRequest.getFunctionName()));
        }
        throw new OFUnexpectedException();
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

    public <R, T> AsyncResponse<R> callAsyncFunction(AsyncRequest<T> asyncRequest, Class<R> returnType) {
        Response response = newClient().build().callAsyncFunction(asyncRequest.getFunctionName(), asyncRequest.getBody());
        if (response.getStatus() == Status.ACCEPTED.getStatusCode()) {
            return AsyncResponse.fromResponse(response, returnType);
        }
        if (response.getStatus() == Status.NOT_FOUND.getStatusCode()) {
            throw new OFNotFoundException(Bundles.getString(NOT_FOUND_MSG, asyncRequest));
        }
        throw new OFUnexpectedException();
    }

}
