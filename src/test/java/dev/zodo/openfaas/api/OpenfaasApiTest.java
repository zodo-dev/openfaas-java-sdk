package dev.zodo.openfaas.api;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

@Slf4j
class OpenfaasApiTest {

    @ParameterizedTest
    @MethodSource("newOpenfaasApi")
    void systemInfoTest(OpenfaasApi openfaasApi) {
        Assertions.assertEquals("faas-netes", openfaasApi.systemInfo().getProvider().getProvider());
    }

    @ParameterizedTest
    @MethodSource("newOpenfaasApi")
    void healthzTest(OpenfaasApi openfaasApi) {
        Assertions.assertTrue(openfaasApi.healthz());
    }

    @ParameterizedTest
    @MethodSource("newOpenfaasApi")
    void listFunctionsTest(OpenfaasApi openfaasApi) {
        Assertions.assertTrue(openfaasApi.listFunctions().isEmpty());
    }

    private static Stream<OpenfaasApi> newOpenfaasApi() {
        return Stream.of(new OpenfaasApi());
    }
}