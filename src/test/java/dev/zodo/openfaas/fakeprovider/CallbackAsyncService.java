package dev.zodo.openfaas.fakeprovider;

import dev.zodo.openfaas.fakeprovider.function.calculator.Calculator;
import dev.zodo.openfaas.fakeprovider.function.calculator.model.CalculatorData;
import dev.zodo.openfaas.fakeprovider.function.calculator.model.ResultData;
import dev.zodo.openfaas.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

@Slf4j
@Service
public class CallbackAsyncService {

    @Async
    public void sendAsyncCallback(String functionName, CalculatorData calculatorData, String callId, String callbackEndpoint) {
        if (Util.isNullOrEmpty(callbackEndpoint)) {
            return;
        }
        ResultData result = ResultData.from(calculatorData, Calculator.calculate(calculatorData));
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        headers.add("X-Call-Id", callId);
        headers.add("X-Function-Name", functionName);
        headers.add("X-Duration-Seconds", "0.150000");
        headers.add("X-Function-Status", "200");
        ClientBuilder
                .newClient()
                .target(callbackEndpoint)
                .request()
                .headers(headers)
                .buildPost(Entity.json(result))
                .invoke();
    }
}
