package dev.zodo.openfaas.api;

import dev.zodo.openfaas.util.Util;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.util.function.Function;

@Slf4j
@Getter
@NoArgsConstructor
public class AsyncResponse<T> {

    private String callId;
    private String functionName;
    private Instant startTime;
    private Double durationSeconds;
    private Long functionStatus;
    private T body;

    public static final <C> AsyncResponse<C> fromResponse(Response res) {
        AsyncResponse<C> asyncResponse = new AsyncResponse<>();
        asyncResponse.callId = getHeaderFromResponse(res, "X-Call-Id", Function.identity());
        asyncResponse.functionName = getHeaderFromResponse(res, "X-Function-Name", Function.identity());
        asyncResponse.functionStatus = getHeaderFromResponse(res, "X-Function-Status", Util::longFromString);
        asyncResponse.durationSeconds = getHeaderFromResponse(res, "X-Duration-Seconds", Util::doubleFromString);
        asyncResponse.startTime = getHeaderFromResponse(res, "X-Function-Status", Util::instantFromStringTimestamp);
        try {
            asyncResponse.body = res.readEntity(new GenericType<>(){});
        } catch (Exception ex) {
            log.warn("Error on parse body request.", ex);
        }
        return asyncResponse;
    }

    private static <T> T getHeaderFromResponse(Response res, String key, Function<String, T> convert) {
        return convert.apply(res.getHeaderString(key));
    }
}
