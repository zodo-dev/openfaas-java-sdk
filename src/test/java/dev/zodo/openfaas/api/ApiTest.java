package dev.zodo.openfaas.api;

import dev.zodo.openfaas.api.async.AsyncCallbackResponse;
import dev.zodo.openfaas.api.async.AsyncRequest;
import dev.zodo.openfaas.api.async.AsyncResponse;
import dev.zodo.openfaas.api.callback.OpenfaasCallbackListener;
import dev.zodo.openfaas.api.model.FunctionInfo;
import dev.zodo.openfaas.api.sync.SyncRequest;
import dev.zodo.openfaas.api.sync.SyncResponse;
import dev.zodo.openfaas.exceptions.OpenfaasSdkNotFoundException;
import dev.zodo.openfaas.fakeprovider.function.calculator.model.CalculatorData;
import dev.zodo.openfaas.fakeprovider.function.calculator.model.Operator;
import dev.zodo.openfaas.fakeprovider.function.calculator.model.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

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

    @BeforeAll
    static void defineLocale() {
        log.info("Locale atual {}", Locale.getDefault().toString());
        Locale.setDefault(new Locale("en", "US"));
    }

    private OpenfaasApi openfaasApi() {
        return OpenfaasApi.getInstance(String.format("http://localhost:%d", this.port), null);
    }

    private OpenfaasApi openfaasApiWithCustomHeader() {
        Supplier<Map<String, String>> headerSupplier = () -> {
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer any_auth_token");
            return headers;
        };
        return OpenfaasApi.getInstance(String.format("http://localhost:%d", this.port), headerSupplier, null);
    }

    private OpenfaasAdminApi openfaasAdminApi() {
        return OpenfaasAdminApi.getInstance(URI.create(String.format("http://localhost:%d", this.port)), username, password, null);
    }

    @Test
    void adminApiSystemInfoTest() {
        Assertions.assertEquals("faas-test", openfaasAdminApi().systemInfo().getProvider().getProvider());
    }

    @Test
    void healthzTest() {
        Assertions.assertTrue(openfaasApi().healthz());
        Assertions.assertTrue(openfaasAdminApi().healthz());
    }

    @Test
    void adminApiListFunctionsTest() {
        final List<FunctionInfo> functionInfos = openfaasAdminApi().listFunctions();
        Assertions.assertEquals(1, functionInfos.size());
        Assertions.assertEquals("calculator", functionInfos.get(0).getName());
    }

    @Test
    void adminApiSystemInfoFunction() {
        Assertions.assertEquals(TEST_FUNCTION_NAME, openfaasAdminApi().infoFunction(TEST_FUNCTION_NAME).getName());
    }

    @Test
    void adminApiSystemInfoFunction404() {
        OpenfaasAdminApi api = openfaasAdminApi();
        OpenfaasSdkNotFoundException e = Assertions.assertThrows(OpenfaasSdkNotFoundException.class, () -> api.infoFunction(TEST_FUNCTION_NAME_404));
        Assertions.assertEquals("Function not found calculator_not_found", e.getMessage());
    }

    @Test
    void callFunctionTest() {
        SyncRequest<CalculatorData> req = new SyncRequest<>(TEST_FUNCTION_NAME, sum10Plus20());
        SyncResponse<ResultData> res = openfaasApi().callFunction(req, ResultData.class);
        Assertions.assertEquals(HttpStatus.OK.value(), res.getStatusCode());
        checkSumResult(res.getBody());
    }

    @Test
    void callFunctionUsingRemoteInterfaceTest() throws ExecutionException, InterruptedException {
        CalculatorRemoteFunction calculatorRemoteFunction = new CalculatorRemoteFunction(String.format("http://localhost:%d", this.port));

        SyncResponse<ResultData> res = calculatorRemoteFunction.call(sum10Plus20());
        Assertions.assertEquals(HttpStatus.OK.value(), res.getStatusCode());
        checkSumResult(res.getBody());

        CompletableFuture<SyncResponse<ResultData>> future = calculatorRemoteFunction.callFuture(mul10Plus20());
        Object o = CompletableFuture.anyOf(future).get();
        Assertions.assertNotNull(o);
        res = (SyncResponse<ResultData>) o;
        Assertions.assertEquals(HttpStatus.OK.value(), res.getStatusCode());
        checkMultiplyResult(res.getBody());

    }

    @Test
    void callFunction404Test() {
        SyncRequest<CalculatorData> req = new SyncRequest<>(TEST_FUNCTION_NAME_404, sum10Plus20());
        OpenfaasApi api = openfaasApi();
        OpenfaasSdkNotFoundException e = Assertions.assertThrows(OpenfaasSdkNotFoundException.class, () -> api.callFunction(req, ResultData.class));
        Assertions.assertEquals("Function not found calculator_not_found", e.getMessage());
    }

    @Test
    void callFunctionFutureTest() throws ExecutionException, InterruptedException {
        SyncRequest<CalculatorData> req = new SyncRequest<>(TEST_FUNCTION_NAME, mul10Plus20());
        CompletableFuture<SyncResponse<ResultData>> future = openfaasApi().callFunctionFuture(req, ResultData.class);
        Object o = CompletableFuture.anyOf(future).get();
        Assertions.assertNotNull(o);
        SyncResponse<ResultData> res = (SyncResponse<ResultData>) o;
        Assertions.assertEquals(HttpStatus.OK.value(), res.getStatusCode());
        checkMultiplyResult(res.getBody());
    }

    @Test
    void z_callAsyncFunctionTest() throws InterruptedException {
        String callbackEndpoint = String.format("http://localhost:%d/api/openfaas/async-callback", port);
        OpenfaasCallbackListener.asyncResponseReceived = null;
        AsyncRequest<CalculatorData> req = new AsyncRequest<>(TEST_FUNCTION_NAME, callbackEndpoint, mul10Plus20());

        AsyncResponse asyncResult = openfaasApiWithCustomHeader().callAsyncFunction(req);
        Assertions.assertEquals(HttpStatus.ACCEPTED.value(), asyncResult.getStatusCode());
        Assertions.assertEquals(TEST_CALL_ID, asyncResult.getCallId());

        // Wait for response in webhook
        TimeUnit.SECONDS.sleep(2);
        Assertions.assertNotNull(OpenfaasCallbackListener.asyncResponseReceived);
        AsyncCallbackResponse<ResultData> res = OpenfaasCallbackListener.asyncResponseReceived;
        Assertions.assertEquals(TEST_FUNCTION_NAME, res.getFunctionName());
        Assertions.assertEquals(TEST_CALL_ID, res.getCallId());
        Assertions.assertEquals(HttpStatus.OK.value(), res.getFunctionStatus());
        checkMultiplyResult(res.getBody());
    }

    @Test
    void z_callAsyncFunctionRemoteInterfaceTest() throws InterruptedException {
        String callbackEndpoint = String.format("http://localhost:%d/api/openfaas/async-callback", port);
        OpenfaasCallbackListener.asyncResponseReceived = null;
        CalculatorRemoteFunction calculatorRemoteFunction = new CalculatorRemoteFunction(String.format("http://localhost:%d", this.port), callbackEndpoint);
        AsyncResponse asyncResult = calculatorRemoteFunction.asyncCall(mul10Plus20());

        Assertions.assertEquals(HttpStatus.ACCEPTED.value(), asyncResult.getStatusCode());
        Assertions.assertEquals(TEST_CALL_ID, asyncResult.getCallId());

        // Wait for response in webhook
        TimeUnit.SECONDS.sleep(2);
        Assertions.assertNotNull(OpenfaasCallbackListener.asyncResponseReceived);
        AsyncCallbackResponse<ResultData> res = OpenfaasCallbackListener.asyncResponseReceived;
        Assertions.assertEquals(TEST_FUNCTION_NAME, res.getFunctionName());
        Assertions.assertEquals(TEST_CALL_ID, res.getCallId());
        Assertions.assertEquals(HttpStatus.OK.value(), res.getFunctionStatus());
        checkMultiplyResult(res.getBody());
    }

    @Test
    void z_callAsyncFunctionNoCallbackTest() throws InterruptedException {
        OpenfaasCallbackListener.asyncResponseReceived = null;
        AsyncRequest<CalculatorData> req = new AsyncRequest<>(TEST_FUNCTION_NAME, null, mul10Plus20());

        AsyncResponse asyncResult = openfaasApi().callAsyncFunction(req);
        Assertions.assertEquals(HttpStatus.ACCEPTED.value(), asyncResult.getStatusCode());
        Assertions.assertEquals(TEST_CALL_ID, asyncResult.getCallId());

        // Wait for response in webhook
        TimeUnit.SECONDS.sleep(2);
        Assertions.assertNull(OpenfaasCallbackListener.asyncResponseReceived);
    }

    @Test
    void z_callAsyncFunction404Test() {
        String callbackEndpoint = String.format("http://localhost:%d/api/openfaas/async-callback", port);
        AsyncRequest<CalculatorData> req = new AsyncRequest<>(TEST_FUNCTION_NAME_404, callbackEndpoint, mul10Plus20());
        OpenfaasApi api = openfaasApi();
        OpenfaasSdkNotFoundException e = Assertions.assertThrows(OpenfaasSdkNotFoundException.class, () -> api.callAsyncFunction(req));
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

    private void checkSumResult(ResultData resultData) {
        Assertions.assertEquals(Operator.SUM, resultData.getOperator());
        Assertions.assertEquals(10d, resultData.getValue1());
        Assertions.assertEquals(20d, resultData.getValue2());
        Assertions.assertEquals(30d, resultData.getResult());
    }

    private void checkMultiplyResult(ResultData resultData) {
        Assertions.assertEquals(Operator.MULTIPLY, resultData.getOperator());
        Assertions.assertEquals(10d, resultData.getValue1());
        Assertions.assertEquals(20d, resultData.getValue2());
        Assertions.assertEquals(200d, resultData.getResult());
    }

    @BeforeAll
    static void runTestAsEnglishLocale() {
        Locale.setDefault(Locale.ENGLISH);
    }

}