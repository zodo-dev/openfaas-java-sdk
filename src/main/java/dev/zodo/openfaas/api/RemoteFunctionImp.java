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

    protected RemoteFunctionImp(String uri, String name, Class<R> returnType, String callbackEndpoint) {
        this.name = name;
        this.returnType = returnType;
        this.uri = uri;
        this.callbackEndpoint = callbackEndpoint;
    }

    protected RemoteFunctionImp(String name, Class<R> returnType) {
        this(null, name, returnType);
    }

    protected RemoteFunctionImp(String uri, String name, Class<R> returnType) {
        this(uri, name, returnType, null);
    }



    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

}
