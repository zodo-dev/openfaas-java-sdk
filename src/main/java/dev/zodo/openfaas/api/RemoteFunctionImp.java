package dev.zodo.openfaas.api;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public abstract class RemoteFunctionImp<T, R> implements RemoteFunction<T, R> {
    @Getter
    private final Map<String, String> headers = new HashMap<>();
    @Getter
    private final String name;
    @Getter
    private final Class<R> returnType;
    @Getter
    @Setter
    private String uri;
    @Getter
    @Setter
    private String callbackEndpoint;

    public RemoteFunctionImp(String name, Class<R> returnType) {
        this.name = name;
        this.returnType = returnType;
    }

    public RemoteFunctionImp(String uri, String name, Class<R> returnType) {
        this(name, returnType);
        this.uri = uri;
    }

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

}
