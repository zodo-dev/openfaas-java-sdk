package dev.zodo.openfaas.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Locale;

class BundlesTest {

    @ParameterizedTest
    @CsvSource(value = {"US;en;Provider does not support info endpoint", "pt;BR;Provedor não disponibiliza informações."}, delimiter = ';')
    void getStringTest(String country, String language, String expected) {
        Locale lc = new Locale(country, language);
        Assertions.assertEquals(expected, Bundles.getString("provider.not.support.endpoint", lc));
        Assertions.assertEquals("Provider does not support info endpoint", Bundles.getString("provider.not.support.endpoint"));
    }

    @Test
    void getStringArgsTest() {
        Assertions.assertEquals("Function not found ABC", Bundles.getString("not.found.function", "ABC"));
    }
}