package dev.zodo.openfaas.fakeprovider;

import lombok.NoArgsConstructor;
import org.apache.http.HttpStatus;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@NoArgsConstructor
public class NotFoundException extends RuntimeException implements ExceptionMapper<NotFoundException> {
    public NotFoundException(String s) {
        super(s);
    }

    @Override
    public Response toResponse(NotFoundException e) {
        EntityResponseError errorEntity = new EntityResponseError(HttpStatus.SC_NOT_FOUND, e.getMessage());
        return Response
                .status(errorEntity.getStatusCode())
                .entity(errorEntity)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
