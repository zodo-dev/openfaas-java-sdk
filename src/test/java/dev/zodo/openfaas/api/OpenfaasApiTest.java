package dev.zodo.openfaas.api;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@Slf4j
class OpenfaasApiTest {

    @Test
    void systemInfoTest() {
        Assertions.assertEquals("faas-netes", OpenfaasApi.getInstance().systemInfo().getProvider().getProvider());
    }

    @Test
    void healthzTest() {
        Assertions.assertTrue(OpenfaasApi.getInstance().healthz());
    }

    @Test
    void listFunctionsTest() {
        Assertions.assertTrue(OpenfaasApi.getInstance().listFunctions().isEmpty());
    }

    @Test
    void callFunctionTest() {
        SyncRequest<String> req = new SyncRequest<>("figlet", "{\"ok\": 1}", Collections.emptyMap());
        final SyncResponse<String> res = OpenfaasApi.getInstance().callFunction(req, String.class);
        Assertions.assertEquals("   ___ _      _    _ _     ___   \n" +
                "  / ( | )___ | | _( | )_  / \\ \\  \n" +
                " | | V V/ _ \\| |/ /V V(_) | || | \n" +
                "< <    | (_) |   <     _  | | > >\n" +
                " | |    \\___/|_|\\_\\   (_) |_|| | \n" +
                "  \\_\\                       /_/  \n", res.getBody());
    }

    @Test
    void callFunctionFutureTest() {
        SyncRequest<String> req = new SyncRequest<>("figlet", "{\"ok\": 1}", Collections.emptyMap());
        final CompletableFuture<SyncResponse<String>> future = OpenfaasApi.getInstance().callFunctionFuture(req, String.class);
        future.thenAccept(res -> {
            Assertions.assertEquals("   ___ _      _    _ _     ___   \n" +
                    "  / ( | )___ | | _( | )_  / \\ \\  \n" +
                    " | | V V/ _ \\| |/ /V V(_) | || | \n" +
                    "< <    | (_) |   <     _  | | > >\n" +
                    " | |    \\___/|_|\\_\\   (_) |_|| | \n" +
                    "  \\_\\                       /_/  \n", res.getBody());
        });
    }

}