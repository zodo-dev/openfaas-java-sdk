package dev.zodo.openfaas.api;

import dev.zodo.openfaas.util.Util;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

@Slf4j
@Getter
@NoArgsConstructor
public class SyncResponse<T> {

    private Double durationSeconds;
    private T body;

    public static final <C> SyncResponse<C> fromResponse(Response res) {
        SyncResponse<C> asyncResponse = new SyncResponse<>();
        asyncResponse.durationSeconds = Util.doubleFromString(res.getHeaderString("X-Duration-Seconds"));
        try {
            asyncResponse.body = res.readEntity(new GenericType<>(){});
        } catch (Exception ex) {
            log.warn("Error on parse body request.", ex);
        }
        return asyncResponse;
    }
}
