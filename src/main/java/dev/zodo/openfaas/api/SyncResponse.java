package dev.zodo.openfaas.api;

import dev.zodo.openfaas.util.Util;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;

@Slf4j
@Getter
public class SyncResponse<T> {

    private Double durationSeconds;
    private T body;

    public static <C> SyncResponse<C> fromResponse(Response res, Class<C> tClass) {
        SyncResponse<C> syncResponse = new SyncResponse<>();
        syncResponse.durationSeconds = Util.doubleFromString(res.getHeaderString("X-Duration-Seconds"));
        try {
            syncResponse.body = res.readEntity(tClass);
        } catch (Exception ex) {
            log.warn("Error on parse body request.", ex);
        }
        return syncResponse;
    }
}
