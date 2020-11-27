package dev.zodo.openfaas.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

class UtilTest {

    @Test
    void bigDecimalFromString() {
        Assertions.assertNull(Util.bigDecimalFromString(""));
        Assertions.assertNull(Util.bigDecimalFromString("AB"));
        Assertions.assertEquals(0, BigDecimal.TEN.compareTo(Util.bigDecimalFromString("10")));
        Assertions.assertEquals(0, BigDecimal.valueOf(10.2).compareTo(Util.bigDecimalFromString("10.2")));
    }

    @Test
    void integerFromString() {
        Assertions.assertNull(Util.integerFromString(""));
        Assertions.assertNull(Util.integerFromString("10.1"));
        Assertions.assertNull(Util.integerFromString("AB"));
        Assertions.assertEquals(10, Util.integerFromString("10"));
    }

    @Test
    void longFromString() {
        Assertions.assertNull(Util.longFromString(""));
        Assertions.assertNull(Util.longFromString("10.1"));
        Assertions.assertNull(Util.longFromString("AB"));
        Assertions.assertEquals(10, Util.longFromString("10"));
    }

    @Test
    void localDateTimeFromStringTimestamp() {
        Assertions.assertNull(Util.localDateTimeFromStringTimestamp(""));
        Assertions.assertNull(Util.localDateTimeFromStringTimestamp("10.1"));
        Assertions.assertNull(Util.localDateTimeFromStringTimestamp("AB"));
        LocalDateTime date = LocalDateTime.parse("2018-12-04T07:24:56").withNano(0);
        LocalDateTime dateFromStr = Util.localDateTimeFromStringTimestamp("1543915495384346700");
        Assertions.assertNotNull(dateFromStr);
        Assertions.assertEquals(date.toString(), dateFromStr.toString());
    }

    @Test
    void durationFromStringTimestamp() {
        Assertions.assertNull(Util.durationFromStringTimestamp(""));
        Assertions.assertNull(Util.durationFromStringTimestamp("AB"));
        Assertions.assertEquals(Duration.ofMillis(500), Util.durationFromStringTimestamp("0.5000000"));
    }

    @Test
    void isNullOrEmpty() {
        Assertions.assertTrue(Util.isNullOrEmpty(""));
        Assertions.assertTrue(Util.isNullOrEmpty(null));
        Assertions.assertFalse(Util.isNullOrEmpty("A"));
    }

}