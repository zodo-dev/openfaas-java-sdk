package dev.zodo.openfaas.fake.callback;

import dev.zodo.openfaas.fake.function.calculator.model.ResultData;
import dev.zodo.openfaas.webhook.CallbackAsyncEndpoint;

import javax.ws.rs.Path;

@Path("/api")
public class CallbackWebhookResource extends CallbackAsyncEndpoint<ResultData> {

}
