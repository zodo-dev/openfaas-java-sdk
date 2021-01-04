package dev.zodo.openfaas.api;

import dev.zodo.openfaas.util.Constants;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

interface BaseApiInterface {
    WebTarget getWebTarget();

    default Entity<Object> convertEntityString(Object obj, MediaType mediaType) {
        if (obj == null) {
            return Entity.entity(null, mediaType);
        }
        try {
            String objStr = Constants.OBJECT_MAPPER.writeValueAsString(obj);
            return Entity.entity(objStr, mediaType);
        } catch (Exception e) {
            return Entity.entity(obj, mediaType);
        }
    }
}
