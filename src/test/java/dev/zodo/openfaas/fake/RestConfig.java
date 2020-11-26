package dev.zodo.openfaas.fake;

import dev.zodo.openfaas.fake.callback.CallbackWebhookResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class RestConfig extends ResourceConfig {

    public RestConfig() {
        register(FakeOpenfassApiResource.class);
        register(CallbackWebhookResource.class);
        register(NotFoundException.class);
    }

}
