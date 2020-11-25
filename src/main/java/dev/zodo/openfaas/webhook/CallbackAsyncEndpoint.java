package dev.zodo.openfaas.webhook;

import dev.zodo.openfaas.api.async.AsyncCallbackResponse;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
public class CallbackAsyncEndpoint<T> {

    @Context
    private OpenfaasCallbackEvent<T> openfaasCallbackEvent;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/openfaas/async-callback")
    public Response asyncCallback(@Context HttpHeaders httpHeaders, T body) {
        if (openfaasCallbackEvent == null) {
            return Response.ok().build();
        }
        try {
            openfaasCallbackEvent.consume(AsyncCallbackResponse.fromRequest(httpHeaders, body));
        } catch (Exception e) {
            log.error("Error on processing received Openfaas callback event.", e);
            return Response.serverError().build();
        }
        return Response.ok().build();
    }
}
