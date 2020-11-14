package dev.zodo.openfaas.util;

import dev.zodo.openfaas.config.Bundles;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;
import java.time.Instant;
import java.util.function.Function;

import static dev.zodo.openfaas.util.Constants.PARSER_VALUE_ERROR;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Util {

    public static Function<Response, String> headerAsString(String key) {
        return response -> response.getHeaderString(key);
    }

    public static Function<Response, Double> headerAsDouble(String key) {
        return response -> {
            String value = response.getHeaderString(key);
            try {
                return Double.parseDouble(value);
            } catch (Exception ex) {
                log.warn(Bundles.getString(PARSER_VALUE_ERROR, value, Double.class.getName()), ex);
                return null;
            }
        };
    }

    public static Double doubleFromString(String str) {
        try {
            return Double.parseDouble(str);
        } catch (Exception ex) {
            log.warn(Bundles.getString(PARSER_VALUE_ERROR, str, Double.class.getName()), ex);
            return null;
        }
    }

    public static Long longFromString(String str) {
        try {
            return Long.parseLong(str);
        } catch (Exception ex) {
            log.warn(Bundles.getString("parser.value.error", str, Double.class.getName()), ex);
            return null;
        }
    }
    public static Instant instantFromStringTimestamp(String str) {
        try {
            Long time = longFromString(str);
            if (time == null) {
                throw new IllegalArgumentException("No time to converter");
            }
            return Instant.ofEpochMilli(time);
        } catch (Exception ex) {
            log.warn(Bundles.getString(PARSER_VALUE_ERROR, str, Double.class.getName()), ex);
            return null;
        }
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

}
