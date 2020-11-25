package dev.zodo.openfaas.api.async;

import dev.zodo.openfaas.util.Util;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.HttpHeaders;
import java.time.Duration;
import java.util.function.Function;

@Slf4j
@Getter
@NoArgsConstructor
public class AsyncCallbackResponse<T> {

    private String callId;
    private String functionName;
    private Duration durationSeconds;
    private Long functionStatus;
    private T body;

    private static <T> T getHeaderFromHttpHeaders(HttpHeaders httpHeaders, String key, Function<String, T> convert) {
        return convert.apply(httpHeaders.getHeaderString(key));
    }

    public static <C> AsyncCallbackResponse<C> fromRequest(HttpHeaders httpHeaders, C obj) {
        AsyncCallbackResponse<C> asyncResponse = new AsyncCallbackResponse<>();
        asyncResponse.callId = getHeaderFromHttpHeaders(httpHeaders, "X-Call-Id", Function.identity());
        asyncResponse.durationSeconds = getHeaderFromHttpHeaders(httpHeaders, "X-Duration-Seconds", Util::durationFromStringTimestamp);
        asyncResponse.functionStatus = getHeaderFromHttpHeaders(httpHeaders, "X-Function-Status", Util::longFromString);
        asyncResponse.functionName = getHeaderFromHttpHeaders(httpHeaders, "X-Function-Name", Function.identity());
        try {
            asyncResponse.body = obj;
        } catch (Exception ex) {
            log.warn("Error on parse body request.", ex);
        }
        return asyncResponse;
    }
}
