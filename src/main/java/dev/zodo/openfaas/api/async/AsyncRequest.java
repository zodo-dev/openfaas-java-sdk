package dev.zodo.openfaas.api.async;

import dev.zodo.openfaas.util.Util;
import lombok.*;

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
    private final Map<String, String> headers = new HashMap<>();

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    public Map<String, String> getHeaders() {
        if (!headers.containsKey("X-Callback-Url") && !Util.isNullOrEmpty(callbackUrl)) {
            addHeader("X-Callback-Url", callbackUrl);
        }
        return headers;
    }
}
