package dev.zodo.openfaas.api;

import dev.zodo.openfaas.api.async.AsyncCallbackResponse;
import dev.zodo.openfaas.api.async.AsyncRequest;
import dev.zodo.openfaas.api.async.AsyncResponse;
import dev.zodo.openfaas.exceptions.OpenfaasSdkNotFoundException;
import dev.zodo.openfaas.api.model.FunctionInfo;
import dev.zodo.openfaas.api.sync.SyncRequest;
import dev.zodo.openfaas.api.sync.SyncResponse;
import dev.zodo.openfaas.api.callback.OpenfaasCallbackListener;
import dev.zodo.openfaas.fakeprovider.function.calculator.model.CalculatorData;
import dev.zodo.openfaas.fakeprovider.function.calculator.model.Operator;
import dev.zodo.openfaas.fakeprovider.function.calculator.model.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class ApiTest {

    private static final String TEST_CALL_ID = "callid_test";
    private static final String TEST_FUNCTION_NAME = "calculator";
    private static final String TEST_FUNCTION_NAME_404 = "calculator_not_found";

    @LocalServerPort
    private int port;

    @Value("${openfaas-java-sdk.admin.username}")
    private String username;

    @Value("${openfaas-java-sdk.admin.password}")
    private String password;

    private OpenfaasApi openfaasApi() {
        return OpenfaasApi.getInstance(String.format("http://localhost:%d", this.port));
    }

    private OpenfaasAdminApi openfaasAdminApi() {
        return OpenfaasAdminApi.getInstance(URI.create(String.format("http://localhost:%d", this.port)), username, password);
    }

    @Test
    void adminApiSystemInfoTest() {
        Assertions.assertEquals("faas-test", openfaasAdminApi().systemInfo().getProvider().getProvider());
    }

    @Test
    void healthzTest() {
        Assertions.assertTrue(openfaasApi().healthz());
    }

    @Test
    void adminApiListFunctionsTest() {
        final List<FunctionInfo> functionInfos = openfaasAdminApi().listFunctions();
        Assertions.assertEquals(1, functionInfos.size());
        Assertions.assertEquals("calculator", functionInfos.get(0).getName());
    }

    @Test
    void callFunctionTest() {
        CalculatorData calculatorData = sum10Plus20();
        SyncRequest<CalculatorData> req = new SyncRequest<>(TEST_FUNCTION_NAME, calculatorData);
        SyncResponse<ResultData> res = openfaasApi().callFunction(req, ResultData.class);
        Assertions.assertEquals(HttpStatus.OK.value(), res.getStatusCode());
        ResultData result = res.getBody();
        Assertions.assertEquals(Operator.SUM, result.getOperator());
        Assertions.assertEquals(10d, result.getValue1());
        Assertions.assertEquals(20d, result.getValue2());
        Assertions.assertEquals(30d, result.getResult());
    }

    @Test
    void callFunction404Test() {
        CalculatorData calculatorData = sum10Plus20();
        SyncRequest<CalculatorData> req = new SyncRequest<>(TEST_FUNCTION_NAME_404, calculatorData);
        OpenfaasApi api = openfaasApi();
        Exception e = Assertions.assertThrows(OpenfaasSdkNotFoundException.class, () -> api.callFunction(req, ResultData.class));
        Assertions.assertEquals("Function not found calculator_not_found", e.getMessage());
    }

    @Test
    void callFunctionFutureTest() throws ExecutionException, InterruptedException {
        CalculatorData calculatorData = mul10Plus20();
        SyncRequest<CalculatorData> req = new SyncRequest<>(TEST_FUNCTION_NAME, calculatorData);
        CompletableFuture<SyncResponse<ResultData>> future = openfaasApi().callFunctionFuture(req, ResultData.class);
        Object o = CompletableFuture.anyOf(future).get();
        Assertions.assertNotNull(o);
        SyncResponse<ResultData> res = (SyncResponse<ResultData>) o;
        Assertions.assertEquals(HttpStatus.OK.value(), res.getStatusCode());
        ResultData result = res.getBody();
        Assertions.assertEquals(Operator.MULTIPLY, result.getOperator());
        Assertions.assertEquals(10d, result.getValue1());
        Assertions.assertEquals(20d, result.getValue2());
        Assertions.assertEquals(200d, result.getResult());
    }

    @Test
    void z_callAsyncFunctionTest() throws InterruptedException {
        CalculatorData calculatorData = mul10Plus20();
        String callbackEndpoint = String.format("http://localhost:%d/api/openfaas/async-callback", port);
        OpenfaasCallbackListener.asyncResponseReceived = null;
        AsyncRequest<CalculatorData> req = new AsyncRequest<>(TEST_FUNCTION_NAME, callbackEndpoint, calculatorData);

        AsyncResponse asyncResult = openfaasApi().callAsyncFunction(req);
        Assertions.assertEquals(HttpStatus.ACCEPTED.value(), asyncResult.getStatusCode());
        Assertions.assertEquals(TEST_CALL_ID, asyncResult.getCallId());

        // Wait for response in webhook
        TimeUnit.SECONDS.sleep(2);
        Assertions.assertNotNull(OpenfaasCallbackListener.asyncResponseReceived);
        AsyncCallbackResponse<ResultData> res = OpenfaasCallbackListener.asyncResponseReceived;
        Assertions.assertEquals(TEST_FUNCTION_NAME, res.getFunctionName());
        Assertions.assertEquals(TEST_CALL_ID, res.getCallId());
        Assertions.assertEquals(HttpStatus.OK.value(), res.getFunctionStatus());

        ResultData result = res.getBody();
        Assertions.assertEquals(Operator.MULTIPLY, result.getOperator());
        Assertions.assertEquals(10d, result.getValue1());
        Assertions.assertEquals(20d, result.getValue2());
        Assertions.assertEquals(200d, result.getResult());
    }

    @Test
    void z_callAsyncFunctionNoCallbackTest() throws InterruptedException {
        CalculatorData calculatorData = mul10Plus20();
        OpenfaasCallbackListener.asyncResponseReceived = null;
        AsyncRequest<CalculatorData> req = new AsyncRequest<>(TEST_FUNCTION_NAME, null, calculatorData);

        AsyncResponse asyncResult = openfaasApi().callAsyncFunction(req);
        Assertions.assertEquals(HttpStatus.ACCEPTED.value(), asyncResult.getStatusCode());
        Assertions.assertEquals(TEST_CALL_ID, asyncResult.getCallId());

        // Wait for response in webhook
        TimeUnit.SECONDS.sleep(2);
        Assertions.assertNull(OpenfaasCallbackListener.asyncResponseReceived);
    }

    @Test
    void z_callAsyncFunction404Test() {
        CalculatorData calculatorData = mul10Plus20();
        String callbackEndpoint = String.format("http://localhost:%d/api/openfaas/async-callback", port);
        AsyncRequest<CalculatorData> req = new AsyncRequest<>(TEST_FUNCTION_NAME_404, callbackEndpoint, calculatorData);
        OpenfaasApi api = openfaasApi();
        Exception e = Assertions.assertThrows(OpenfaasSdkNotFoundException.class, () -> api.callAsyncFunction(req));
        Assertions.assertEquals("Function not found calculator_not_found", e.getMessage());
    }


    private CalculatorData sum10Plus20() {
        return CalculatorData.builder()
                .operator(Operator.SUM)
                .value1(10d)
                .value2(20d)
                .build();
    }

    private CalculatorData mul10Plus20() {
        return CalculatorData.builder()
                .operator(Operator.MULTIPLY)
                .value1(10d)
                .value2(20d)
                .build();
    }

}