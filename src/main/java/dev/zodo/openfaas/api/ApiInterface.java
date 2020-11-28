package dev.zodo.openfaas.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

interface ApiInterface {

    @GET
    @Path("/healthz")
    @Consumes(MediaType.APPLICATION_JSON)
    Response healthz();

    @POST
    @Path("/function/{functionName}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    Response callFunction(@PathParam("functionName") String functionName, Object body);

    @POST
    @Path("/async-function/{functionName}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    Response callAsyncFunction(@PathParam("functionName") String functionName, Object body);

}
