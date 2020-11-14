package dev.zodo.openfaas.api;

import dev.zodo.openfaas.util.Util;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AsyncRequest<T> {
    private String functionName;
    private String callbackUrl;
    private T body;
    private Map<String, String> headers;

    public void addHeader(String name, String value) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(name, value);
    }

    public void configureClientBuilderHeaders(ApiClientBuilder<?> apiClientBuilder) {
        apiClientBuilder.addHeaders(headers);
        if (!Util.isNullOrEmpty(callbackUrl)) {
            apiClientBuilder.addHeader("X-Callback-Url", callbackUrl);
        }
    }

}
