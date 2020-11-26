package dev.zodo.openfaas.fake;

import dev.zodo.openfaas.fake.function.calculator.Calculator;
import dev.zodo.openfaas.fake.function.calculator.model.CalculatorData;
import dev.zodo.openfaas.fake.function.calculator.model.ResultData;
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
