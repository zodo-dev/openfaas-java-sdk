package dev.zodo.openfaas.fakeprovider.function.calculator;

import dev.zodo.openfaas.fakeprovider.function.calculator.model.CalculatorData;

public final class Calculator {
    private Calculator() {
    }

    public static Double calculate(CalculatorData data) {
        if (data == null || data.getOperator() == null) {
            return null;
        }
        return data.getOperator().calculate(data.getValue1(), data.getValue2());
    }
}
