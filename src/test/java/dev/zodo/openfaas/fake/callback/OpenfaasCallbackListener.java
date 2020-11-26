package dev.zodo.openfaas.fake.callback;

import dev.zodo.openfaas.api.async.AsyncCallbackResponse;
import dev.zodo.openfaas.fake.function.calculator.model.ResultData;
import dev.zodo.openfaas.webhook.OpenfaasCallbackEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OpenfaasCallbackListener implements OpenfaasCallbackEvent<ResultData> {

    public static AsyncCallbackResponse<ResultData> asyncResponseReceived;

    @Override
    public void consume(AsyncCallbackResponse<ResultData> asyncResponse) {
        log.info("## Webhook: Receive event: {}", asyncResponse.getFunctionName());
        asyncResponseReceived = asyncResponse;
    }
}
