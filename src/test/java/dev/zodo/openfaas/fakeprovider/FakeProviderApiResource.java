package dev.zodo.openfaas.fakeprovider;

import dev.zodo.openfaas.api.model.FunctionInfo;
import dev.zodo.openfaas.api.model.Info;
import dev.zodo.openfaas.api.model.ProviderInfo;
import dev.zodo.openfaas.api.model.Version;
import dev.zodo.openfaas.fakeprovider.function.calculator.Calculator;
import dev.zodo.openfaas.fakeprovider.function.calculator.model.CalculatorData;
import dev.zodo.openfaas.fakeprovider.function.calculator.model.ResultData;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;

@Path("/")
public class FakeProviderApiResource {

    private static final String TEST_FUNCTION = "calculator";

    @Context
    private CallbackAsyncService callbackAsyncService;

    @GET
    @Path("/healthz")
    @Produces(MediaType.APPLICATION_JSON)
    public Response healthz() {
        return Response
                .ok()
                .build();
    }

    @GET
    @Path("/system/info")
    @Produces(MediaType.APPLICATION_JSON)
    public Response systemInfo() {
        Version version = new Version("Fake version", "123456", "123456789", "fake");
        ProviderInfo providerInfo = new ProviderInfo("faas-test", "faas-test", "fake", version);
        return Response
                .ok(new Info(providerInfo, version, "x64"))
                .build();
    }

    @GET
    @Path("/system/functions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listFunctions() {
        return Response
                .ok(Collections.singletonList(FunctionInfo.builder().name(TEST_FUNCTION).build()))
                .build();
    }

    @POST
    @Path("/system/function/{functionName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response infoFunction(@PathParam("functionName") String functionName) {
        return Response
                .noContent()
                .build();
    }

    @POST
    @Path("/system/scale-function/{functionName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response scaleFunction(@PathParam("functionName") String functionName, Object body) {
        return Response
                .noContent()
                .build();
    }

    @POST
    @Path("/function/{functionName}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response callFunction(@PathParam("functionName") String functionName, CalculatorData calculatorData) {
        if (!TEST_FUNCTION.equals(functionName)) {
            throw new NotFoundException(String.format("Function not found [{%s}]", functionName));
        }
        return Response.ok(ResultData.from(calculatorData, Calculator.calculate(calculatorData))).build();
    }

    @POST
    @Path("/async-function/{functionName}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response callAsyncFunction(@PathParam("functionName") String functionName,
                                               @HeaderParam("X-Callback-Url") String callbackEndpoint,
                                               CalculatorData calculatorData) {
        if (!TEST_FUNCTION.equals(functionName)) {
            throw new NotFoundException(String.format("Function not found [{%s}]", functionName));
        }
        LocalDateTime now = LocalDateTime.now();
        String callId = "callid_test";
        callbackAsyncService.sendAsyncCallback(functionName, calculatorData, callId, callbackEndpoint);
        long startTime = now.atZone(ZoneId.systemDefault()).toEpochSecond() * 999_999_999;
        return Response.accepted()
                .header("X-Call-Id", callId)
                .header("X-Start-Time", String.valueOf(startTime))
                .build();
    }

}
