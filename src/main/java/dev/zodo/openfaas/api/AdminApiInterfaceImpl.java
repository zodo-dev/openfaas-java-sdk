package dev.zodo.openfaas.api;

import dev.zodo.openfaas.api.model.FunctionInfo;
import lombok.Getter;

import javax.ws.rs.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class AdminApiInterfaceImpl implements AdminApiInterface {

    @Getter
    private final WebTarget webTarget;

    AdminApiInterfaceImpl(WebTarget webTarget) {
        this.webTarget = webTarget;
    }

    @GET
    @Path("/healthz")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response healthz() {
        return getWebTarget()
                .path("/healthz")
                .request(MediaType.APPLICATION_JSON)
                .get();
    }

    @GET
    @Path("/system/info")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response systemInfo() {
        return getWebTarget()
                .path("/system/info")
                .request(MediaType.APPLICATION_JSON)
                .get();
    }

    @Override
    public List<FunctionInfo> listFunctions() {
        return getWebTarget()
                .path("/system/functions")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<FunctionInfo>>() {
                });
    }

    @Override
    public Response infoFunction(String functionName) {
        String path = String.format("/system/function/%s", functionName);
        return getWebTarget()
                .path(path)
                .request(MediaType.APPLICATION_JSON)
                .get();
    }

    @Override
    public Response scaleFunction(String functionName, Object body) {
        String path = String.format("/system/scale-function/%s", functionName);
        Entity<?> entity = convertEntityString(body, body instanceof String ? MediaType.TEXT_PLAIN_TYPE : MediaType.APPLICATION_JSON_TYPE);
        return getWebTarget()
                .path(path)
                .request()
                .post(entity);
    }

}
