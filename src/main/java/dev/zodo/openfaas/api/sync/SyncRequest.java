package dev.zodo.openfaas.api.sync;

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
    private final Map<String, String> headers = new HashMap<>();
    public void addHeader(String name, String value) {
        headers.put(name, value);
    }
}
