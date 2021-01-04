package dev.zodo.openfaas.api;

import lombok.Getter;

import javax.ws.rs.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ApiInterfaceImpl implements ApiInterface {

    @Getter
    private final WebTarget webTarget;

    ApiInterfaceImpl(WebTarget webTarget) {
        this.webTarget = webTarget;
    }

    @GET
    @Path("/healthz")
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public Response healthz() {
        return getWebTarget()
                .path("/healthz")
                .request(MediaType.APPLICATION_JSON)
                .get();
    }

    @Override
    public Response callFunction(String functionName, Object body) {
        String path = String.format("/function/%s", functionName);
        Entity<?> entity = convertEntityString(body, body instanceof String ? MediaType.TEXT_PLAIN_TYPE : MediaType.APPLICATION_JSON_TYPE);
        return getWebTarget()
                .path(path)
                .request()
                .post(entity);
    }

    @Override
    public Response callAsyncFunction(String functionName, Object body) {
        String path = String.format("/async-function/%s", functionName);
        Entity<?> entity = convertEntityString(body, body instanceof String ? MediaType.TEXT_PLAIN_TYPE : MediaType.APPLICATION_JSON_TYPE);
        return getWebTarget()
                .path(path)
                .request()
                .post(entity);
    }

}
