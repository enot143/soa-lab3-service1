package itmo.client.exceptions;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {

    @Override
    public Response toResponse(final BadRequestException e){
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorMessage(e.getMessage()))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}