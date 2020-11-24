package dev.zodo.openfaas.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.ConfigCache;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Sources({"classpath:openfaas-sdk.properties", "classpath:openfaas-sdk-default.properties"})
public interface OpenfaasSdkProperties extends Config {
    OpenfaasSdkProperties OPENFAAS_PROPS = ConfigCache.getOrCreate(OpenfaasSdkProperties.class);

    @Key("openfaas.url")
    String url();

    @Key("openfaas.username")
    String username();

    @Key("openfaas.password")
    String password();

    @Key("openfaas.callback.async.endpoint.prefix")
    String openfaasCallbackAsyncEndpointPrefix();
}
