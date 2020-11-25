package dev.zodo.openfaas.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BundlesTest {
    @Test
    void getStringTest() {
        Assertions.assertEquals("Provider does not support info endpoint", Bundles.getString("provider.not.support.endpoint"));
    }

    @Test
    void getStringArgsTest() {
        Assertions.assertEquals("Function not found ABC", Bundles.getString("not.found.function", "ABC"));
    }
}