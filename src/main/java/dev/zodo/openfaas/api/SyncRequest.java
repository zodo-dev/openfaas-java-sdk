package dev.zodo.openfaas.api;

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
public class SyncRequest<T> {
    private String functionName;
    private T body;
    private Map<String, String> headers;
    public void addHeader(String name, String value) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(name, value);
    }
}
