package dev.zodo.openfaas.i18n;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Locale;

@Slf4j
class BundlesTest {

    @BeforeAll
    static void defineLocale() {
        log.info("Locale atual {}", Locale.getDefault().toString());
        Locale.setDefault(new Locale("en", "US"));
    }

    @ParameterizedTest
    @CsvSource(value = {"US;en;Provider does not support info endpoint", "BR;pt;Provedor não disponibiliza informações."}, delimiter = ';')
    void getStringTest(String country, String language, String expected) {
        Locale lc = new Locale(language, country);
        Assertions.assertEquals(expected, Bundles.getString("provider.not.support.endpoint", lc));
        Assertions.assertEquals("Provider does not support info endpoint", Bundles.getString("provider.not.support.endpoint"));
    }

    @Test
    void getStringArgsTest() {
        Assertions.assertEquals("Function not found ABC", Bundles.getString("not.found.function", "ABC"));
    }

    @Test
    void getStringNotImplTest() {
        Assertions.assertEquals("bundle.not.impl", Bundles.getString("bundle.not.impl"));
        Assertions.assertEquals("test.empty.str", Bundles.getString("test.empty.str", new Locale("pt", "BR")));
        Assertions.assertEquals("test.empty.str", Bundles.getString("test.empty.str", new Locale("pt", "PT")));
    }

    @BeforeAll
    static void usingTestBundle() {
        Bundles.loadBundles(BundlesTest.class.getClassLoader());
    }
}