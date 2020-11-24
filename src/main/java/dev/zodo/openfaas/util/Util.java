package dev.zodo.openfaas.util;

import dev.zodo.openfaas.config.Bundles;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static dev.zodo.openfaas.util.Constants.PARSER_VALUE_ERROR;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Util {

    public static BigDecimal bigDecimalFromString(String str) {
        if (isNullOrEmpty(str)) {
            return null;
        }
        try {
            return BigDecimal.valueOf(Double.parseDouble(str));
        } catch (Exception ex) {
            log.warn(Bundles.getString(PARSER_VALUE_ERROR, str, BigDecimal.class.getName()), ex);
            return null;
        }
    }

    public static Long longFromString(String str) {
        if (isNullOrEmpty(str)) {
            return null;
        }
        try {
            return isNullOrEmpty(str) ? null : Long.parseLong(str);
        } catch (Exception ex) {
            log.warn(Bundles.getString("parser.value.error", str, Long.class.getName()), ex);
            return null;
        }
    }

    public static LocalDateTime localDateTimeFromStringTimestamp(String str) {
        try {
            Long time = longFromString(str);
            if (time == null) {
                throw new IllegalArgumentException("No time to converter");
            }
            return Instant.ofEpochSecond(time / 999999999).atZone(ZoneId.systemDefault()).toLocalDateTime();
        } catch (Exception ex) {
            log.warn(Bundles.getString(PARSER_VALUE_ERROR, str, LocalDateTime.class.getName()), ex);
            return null;
        }
    }

    public static Duration durationFromStringTimestamp(String str) {
        try {
            BigDecimal seconds = bigDecimalFromString(str);
            if (seconds == null) {
                return null;
            }
            return Duration.ofMillis(seconds.multiply(BigDecimal.valueOf(1_000_000)).longValue());
        } catch (Exception ex) {
            log.warn(Bundles.getString(PARSER_VALUE_ERROR, str, Duration.class.getName()), ex);
            return null;
        }
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

}
