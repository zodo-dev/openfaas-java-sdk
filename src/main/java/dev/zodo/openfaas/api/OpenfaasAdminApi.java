package dev.zodo.openfaas.api;

import dev.zodo.openfaas.api.model.FunctionInfo;
import dev.zodo.openfaas.api.model.Info;
import dev.zodo.openfaas.exceptions.OpenfaasSdkNotFoundException;
import dev.zodo.openfaas.exceptions.OpenfaasSdkUnexpectedException;
import dev.zodo.openfaas.i18n.Bundles;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.URI;
import java.util.List;

import static dev.zodo.openfaas.util.Constants.NOT_FOUND_MSG;

@Slf4j
public final class OpenfaasAdminApi extends BaseApi<AdminApiInterface> {

    private OpenfaasAdminApi(URI uri, String username, String password, ResteasyClient client) {
        super(uri, username, password, AdminApiInterface.class, client);
    }

    public static OpenfaasAdminApi getInstance(URI uri, String username, String password, ResteasyClient client) {
        return new OpenfaasAdminApi(uri, username, password, client);
    }

    public boolean healthz() {
        final Response response = newClient().build().healthz();
        return response.getStatus() == Status.OK.getStatusCode();
    }

    public Info systemInfo() {
        Response response = newClient(true).build().systemInfo();
        if (response.getStatus() == Status.OK.getStatusCode()) {
            return response.readEntity(Info.class);
        }
        if (response.getStatus() == Status.NOT_FOUND.getStatusCode()) {
            throw new OpenfaasSdkNotFoundException(Bundles.getString("provider.not.support.endpoint"));
        }
        throw new OpenfaasSdkUnexpectedException();
    }

    public List<FunctionInfo> listFunctions() {
        return newClient(true).build().listFunctions();
    }

    public FunctionInfo infoFunction(String functionName) {
        Response response = newClient(true).build().infoFunction(functionName);
        if (response.getStatus() == Status.OK.getStatusCode()) {
            return response.readEntity(FunctionInfo.class);
        }
        if (response.getStatus() == Status.NOT_FOUND.getStatusCode()) {
            throw new OpenfaasSdkNotFoundException(Bundles.getString(NOT_FOUND_MSG, functionName));
        }
        throw new OpenfaasSdkUnexpectedException();
    }

}
