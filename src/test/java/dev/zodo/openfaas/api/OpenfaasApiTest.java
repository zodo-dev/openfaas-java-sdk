package dev.zodo.openfaas.api;

import dev.zodo.openfaas.api.async.AsyncCallbackResponse;
import dev.zodo.openfaas.api.async.AsyncRequest;
import dev.zodo.openfaas.api.async.AsyncResponse;
import dev.zodo.openfaas.api.model.FunctionInfo;
import dev.zodo.openfaas.api.sync.SyncRequest;
import dev.zodo.openfaas.api.sync.SyncResponse;
import dev.zodo.openfaas.fake.callback.OpenfaasCallbackListener;
import dev.zodo.openfaas.fake.function.calculator.model.CalculatorData;
import dev.zodo.openfaas.fake.function.calculator.model.Operator;
import dev.zodo.openfaas.fake.function.calculator.model.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static dev.zodo.openfaas.config.OpenfaasSdkProperties.OPENFAAS_PROPS;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class OpenfaasApiTest {

    private static final String TEST_CALL_ID = "callid_test";
    private static final String TEST_FUNCTION_NAME = "calculator";

    @LocalServerPort
    private int port;

    private OpenfaasApi openfaasApi() {
        return OpenfaasApi.getInstance(String.format("http://localhost:%d/", this.port), OPENFAAS_PROPS.username(), OPENFAAS_PROPS.username());
    }

    @Test
    void systemInfoTest() {
        Assertions.assertEquals("faas-test", openfaasApi().systemInfo().getProvider().getProvider());
    }

    @Test
    void healthzTest() {
        Assertions.assertTrue(openfaasApi().healthz());
    }

    @Test
    void listFunctionsTest() {
        final List<FunctionInfo> functionInfos = openfaasApi().listFunctions();
        Assertions.assertEquals(1, functionInfos.size());
        Assertions.assertEquals("calculator", functionInfos.get(0).getName());
    }

    @Test
    void callFunctionTest() {
        CalculatorData calculatorData = CalculatorData.builder()
                .operator(Operator.SUM)
                .value1(10d)
                .value2(20d)
                .build();
        SyncRequest<CalculatorData> req = new SyncRequest<>(TEST_FUNCTION_NAME, calculatorData);
        SyncResponse<ResultData> res = openfaasApi().callFunction(req, ResultData.class);
        ResultData result = res.getBody();
        Assertions.assertEquals(Operator.SUM, result.getOperator());
        Assertions.assertEquals(10d, result.getValue1());
        Assertions.assertEquals(20d, result.getValue2());
        Assertions.assertEquals(30d, result.getResult());
    }

    @Test
    void callFunctionFutureTest() throws ExecutionException, InterruptedException {
        CalculatorData calculatorData = CalculatorData.builder()
                .operator(Operator.MULTIPLY)
                .value1(10d)
                .value2(20d)
                .build();
        SyncRequest<CalculatorData> req = new SyncRequest<>(TEST_FUNCTION_NAME, calculatorData);

        CompletableFuture<SyncResponse<ResultData>> future = openfaasApi().callFunctionFuture(req, ResultData.class);
        Object o = CompletableFuture.anyOf(future).get();
        Assertions.assertNotNull(o);
        SyncResponse<ResultData> res = (SyncResponse<ResultData>) o;
        ResultData result = res.getBody();
        Assertions.assertEquals(Operator.MULTIPLY, result.getOperator());
        Assertions.assertEquals(10d, result.getValue1());
        Assertions.assertEquals(20d, result.getValue2());
        Assertions.assertEquals(200d, result.getResult());
    }

    @Test
    void z_callAsyncFunctionTest() throws InterruptedException {
        CalculatorData calculatorData = CalculatorData.builder()
                .operator(Operator.MULTIPLY)
                .value1(10d)
                .value2(20d)
                .build();
        String callbackEndpoint = String.format("http://localhost:%d/api/openfaas/async-callback", port);
        OpenfaasCallbackListener.asyncResponseReceived = null;
        AsyncRequest<CalculatorData> req = new AsyncRequest<>(TEST_FUNCTION_NAME, callbackEndpoint, calculatorData);

        AsyncResponse asyncResult = openfaasApi().callAsyncFunction(req);
        Assertions.assertEquals(HttpStatus.ACCEPTED.value(), asyncResult.getStatusCode());
        Assertions.assertEquals(TEST_CALL_ID, asyncResult.getCallId());

        // Wait for response in webhook
        TimeUnit.SECONDS.sleep(5);
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

}