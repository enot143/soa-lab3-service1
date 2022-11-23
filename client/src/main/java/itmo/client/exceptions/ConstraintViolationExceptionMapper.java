package itmo.client.exceptions;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashSet;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException>{

    @Override
    public Response toResponse(final ConstraintViolationException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(prepareMessage(exception))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }



    private HashSet<?> prepareMessage(ConstraintViolationException exception) {
        HashSet<ErrorMessage> array = new HashSet<>();
        for (ConstraintViolation<?> cv : exception.getConstraintViolations()) {
          array.add(new ErrorMessage(cv.getMessage()));
        }
        return array;
    }

}