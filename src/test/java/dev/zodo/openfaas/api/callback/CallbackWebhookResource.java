package dev.zodo.openfaas.api.callback;

import dev.zodo.openfaas.fakeprovider.function.calculator.model.ResultData;
import dev.zodo.openfaas.webhook.CallbackAsyncEndpoint;

import javax.ws.rs.Path;

@Path("/api")
public class CallbackWebhookResource extends CallbackAsyncEndpoint<ResultData> {

}
