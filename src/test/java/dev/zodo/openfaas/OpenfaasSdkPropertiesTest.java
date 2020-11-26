package dev.zodo.openfaas;

import org.junit.jupiter.api.Test;

import static dev.zodo.openfaas.config.OpenfaasSdkProperties.OPENFAAS_PROPS;
import static org.junit.jupiter.api.Assertions.*;

class OpenfaasSdkPropertiesTest {

    @Test
    void checkDefaultValueTest() {
        assertEquals("http://127.0.0.1:31112", OPENFAAS_PROPS.url());
        assertEquals("admin", OPENFAAS_PROPS.username());
        assertEquals("admin", OPENFAAS_PROPS.password());
    }

}