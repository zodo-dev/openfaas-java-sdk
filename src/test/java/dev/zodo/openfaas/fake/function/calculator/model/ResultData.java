package dev.zodo.openfaas.fake.function.calculator.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultData {
    private Operator operator;
    private Double value1;
    private Double value2;
    private Double result;

    public static ResultData from(CalculatorData data, Double result) {
        final ResultData resultData;
        if (data == null) {
            resultData = new ResultData(null, null, null, result);
        } else {
            resultData = new ResultData(data.getOperator(), data.getValue1(), data.getValue2(), result);
        }
        return resultData;
    }
}
