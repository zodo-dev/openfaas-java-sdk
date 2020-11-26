package dev.zodo.openfaas.api.async;

import dev.zodo.openfaas.util.Util;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.function.Function;

@Slf4j
@Getter
@NoArgsConstructor
public class AsyncResponse {

    private String callId;
    private LocalDateTime startTime;
    private Integer statusCode;

    public static AsyncResponse fromResponse(Response res) {
        AsyncResponse asyncResponse = new AsyncResponse();
        asyncResponse.callId = getHeaderFromResponse(res, "X-Call-Id", Function.identity());
        asyncResponse.startTime = getHeaderFromResponse(res, "X-Start-Time", Util::localDateTimeFromStringTimestamp);
        try {
            asyncResponse.statusCode = res.getStatus();
        } catch (Exception ex) {
            log.warn("Error on parse body request.", ex);
        }
        return asyncResponse;
    }

    private static <T> T getHeaderFromResponse(Response res, String key, Function<String, T> convert) {
        return convert.apply(res.getHeaderString(key));
    }
}
