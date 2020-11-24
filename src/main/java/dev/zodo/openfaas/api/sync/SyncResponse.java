package dev.zodo.openfaas.api.sync;

import dev.zodo.openfaas.util.Util;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;
import java.time.Duration;

@Slf4j
@Getter
public class SyncResponse<T> {

    private Duration durationSeconds;
    private T body;
    private Integer statusCode;

    public static <C> SyncResponse<C> fromResponse(Response res, Class<C> tClass) {
        SyncResponse<C> syncResponse = new SyncResponse<>();
        syncResponse.durationSeconds = Util.durationFromStringTimestamp(res.getHeaderString("X-Duration-Seconds"));
        try {
            syncResponse.statusCode = res.getStatus();
            if (res.hasEntity()) {
                syncResponse.body = res.readEntity(tClass);
            }
        } catch (Exception ex) {
            log.warn("Error on parse body request.", ex);
        }
        return syncResponse;
    }
}
