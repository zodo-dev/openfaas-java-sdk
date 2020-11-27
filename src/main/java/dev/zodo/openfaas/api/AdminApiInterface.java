package dev.zodo.openfaas.api;

import dev.zodo.openfaas.api.model.FunctionInfo;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

interface AdminApiInterface {

    @GET
    @Path("/healthz")
    @Consumes(MediaType.APPLICATION_JSON)
    Response healthz();

    @GET
    @Path("/system/info")
    @Consumes(MediaType.APPLICATION_JSON)
    Response systemInfo();

    @GET
    @Path("/system/functions")
    @Consumes(MediaType.APPLICATION_JSON)
    List<FunctionInfo> listFunctions();

    @GET
    @Path("/system/function/{functionName}")
    @Consumes(MediaType.APPLICATION_JSON)
    Response infoFunction(@PathParam("functionName") String functionName);

    @POST
    @Path("/system/scale-function/{functionName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response scaleFunction(@PathParam("functionName") String functionName, Object body);

}
