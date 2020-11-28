package dev.zodo.openfaas.fakeprovider;

import dev.zodo.openfaas.api.callback.CallbackWebhookResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class RestConfig extends ResourceConfig {

    public RestConfig() {
        register(FakeProviderApiResource.class);
        register(CallbackWebhookResource.class);
        register(NotFoundException.class);
    }

}
