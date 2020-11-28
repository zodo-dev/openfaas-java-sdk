package dev.zodo.openfaas.api;

import dev.zodo.openfaas.fakeprovider.function.calculator.model.CalculatorData;
import dev.zodo.openfaas.fakeprovider.function.calculator.model.ResultData;

public class CalculatorRemoteFunction extends RemoteFunctionImp<CalculatorData, ResultData> {
    public CalculatorRemoteFunction(String uri) {
        super(uri, "calculator", ResultData.class);
    }

    public CalculatorRemoteFunction(String uri, String callbackEndpoint) {
        super(uri, "calculator", ResultData.class, callbackEndpoint);
    }
}
